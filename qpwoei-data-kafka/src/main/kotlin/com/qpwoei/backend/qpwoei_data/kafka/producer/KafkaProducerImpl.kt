package com.qpwoei.backend.qpwoei_data.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.function.BiConsumer

@Component
class KafkaProducerImpl <K: Serializable, V: Serializable> (
    val kafkaTemplate: KafkaTemplate<K, V>
) : KafkaProducer<K, V> {
    override fun send(topicName: String, key: K, message: V, consumer: BiConsumer<SendResult<K, V>, Throwable>) {
        val kafkaResultFuture = kafkaTemplate.send(
            topicName, key, message
        )

        kafkaResultFuture.whenComplete(consumer)
    }
}