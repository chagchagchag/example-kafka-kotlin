package com.qpwoei.backend.qpwoei_data.kafka.integration_test

import com.fasterxml.jackson.databind.ObjectMapper
import com.qpwoei.backend.qpwoei_data.kafka.integration_test.sample.SampleOutboxMessage
import com.qpwoei.backend.qpwoei_data.kafka.integration_test.sample.SampleRequestKafkaModel
import com.qpwoei.backend.qpwoei_data.kafka.producer.KafkaMessageHelper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.ContainerTestUtils
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092","port=9092"])
@SpringBootTest
class EmbeddedKafkaProducerTest {
    val logger = LoggerFactory.getLogger(javaClass)

    val TEST_REQUEST_TOPIC_NAME = "EMBEDDED-UNIT-TEST-TOPIC-REQUEST"
    val TEST_RESPONSE_TOPIC_NAME = "EMBEDDED-UNIT-TEST-TOPIC-RESPONSE"

    @Autowired
    lateinit var kafkaMessageHelper: KafkaMessageHelper

    @Autowired
    lateinit var kafkaTemplate : KafkaTemplate<String, String>


    lateinit var container : KafkaMessageListenerContainer<String, String>

    lateinit var embeddedKafkaBroker : EmbeddedKafkaBroker

    val consumerRecords: BlockingQueue<ConsumerRecord<String, String>> =
        LinkedBlockingQueue<ConsumerRecord<String, String>>()

    @BeforeEach
    fun init(){
        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(consumerConfigMap())
        val containerProperties = ContainerProperties(TEST_REQUEST_TOPIC_NAME)
        val messageListener: MessageListener<String, String> = MessageListener {
            logger.info("Listened Message = ${it.toString()}")
            consumerRecords.add(it)
        }

        container = KafkaMessageListenerContainer<String, String>(consumerFactory, containerProperties)
        container.setupMessageListener(messageListener)
        container.start()

        embeddedKafkaBroker = EmbeddedKafkaBroker(
            2, true, 2, TEST_REQUEST_TOPIC_NAME
        )

        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.partitionsPerTopic)
    }

    @AfterEach
    fun destroy(){
        container.stop()
    }

    @Test
    fun 샘플데이터전송_EmbeddedKafka_테스트(){
        val outboxMessage = SampleOutboxMessage(
            name = "테스트",
            message = "안녕하세요",
            sagaId = UUID.randomUUID(),
        )

        val requestModel = toRequest(outboxMessage)
        val payload = ObjectMapper().writeValueAsString(requestModel)

        val callback = kafkaMessageHelper.handleKafkaSendResult(
            responseTopicName = TEST_RESPONSE_TOPIC_NAME,
            requestModel = payload
        )

        val future = kafkaTemplate.send(TEST_REQUEST_TOPIC_NAME, outboxMessage.sagaId.toString(), payload)
        future.whenComplete(callback)

        Thread.sleep(3000L)

        val received : ConsumerRecord<String, String>? =
            consumerRecords.poll(5, TimeUnit.SECONDS)

        println("received = $received")
    }

    fun toRequest(sampleOutboxMessage: SampleOutboxMessage) =
        SampleRequestKafkaModel(
            id = UUID.randomUUID(),
            message = "카프카 동작확인을 위한 더미 기능",
            sagaId = UUID.randomUUID(),
        )
    
    fun consumerConfigMap() = mapOf(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
        ConsumerConfig.GROUP_ID_CONFIG to "sample-group",
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
        ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG to 10,
        ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG to 60000,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to "org.apache.kafka.common.serialization.StringDeserializer",
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
    )
}