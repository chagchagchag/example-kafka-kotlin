package com.qpwoei.backend.qpwoei_data.kafka

import com.qpwoei.backend.qpwoei_data.kafka.properties.KafkaConsumerProperties
import com.qpwoei.backend.qpwoei_data.kafka.properties.KafkaProducerProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class CommandLineRunnerTemp (
        val kafkaConsumerProperties: KafkaConsumerProperties,
        val kafkaProducerProperties: KafkaProducerProperties,
): CommandLineRunner{

    val logger = LoggerFactory.getLogger(javaClass)

    override fun run(vararg args: String?) {
        logger.info("(consumer) props test = ${kafkaConsumerProperties.keyDeserializer}")
        logger.info("(producer) props test = ${kafkaProducerProperties.keySerializerClass}")
    }

}