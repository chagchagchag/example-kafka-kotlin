package com.qpwoei.backend.qpwoei_data.kafka.producer

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.function.BiConsumer

@Component
class KafkaMessageHelper (
    val objectMapper: ObjectMapper
){
    val log = LoggerFactory.getLogger(javaClass)

    fun <T> handleKafkaSendResult(responseTopicName: String, requestModel: T) = BiConsumer(){
            sendResult: SendResult<String, T>, throwable: Throwable ->
        if(throwable == null){
            handleSuccess(responseTopicName, sendResult)
        }
        else{
            handleFailure(responseTopicName, sendResult, throwable)
        }
    }

    fun <T> handleSuccess(responseTopicName: String, sendResult: SendResult<String, T>) : Unit{
        val metadata: RecordMetadata = sendResult.recordMetadata!!

        log.info("Received successful response from Kafka for " +
            "Topic ${metadata.topic()} " +
            "Partition ${metadata.partition()}" +
            "Offset: ${metadata.offset()}" +
            "Timestamp: ${metadata.timestamp()}")
    }

    fun <T> handleFailure(responseTopicName: String, sendResult: SendResult<String, T>, throwable: Throwable) : Unit{
        log.error("Error while sending " +
            "message ==> ${sendResult.producerRecord.value()} to topic $responseTopicName", throwable
        )
    }
}