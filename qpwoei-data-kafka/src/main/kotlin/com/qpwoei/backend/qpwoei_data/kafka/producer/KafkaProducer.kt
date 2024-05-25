package com.qpwoei.backend.qpwoei_data.kafka.producer

import org.springframework.kafka.support.SendResult
import java.io.Serializable
import java.util.function.BiConsumer

interface KafkaProducer <K : Serializable, V : Serializable> {
    fun send (topicName: String,
              key: K, message: V,
              consumer: BiConsumer<SendResult<K, V>, Throwable>
    ) : Unit
}