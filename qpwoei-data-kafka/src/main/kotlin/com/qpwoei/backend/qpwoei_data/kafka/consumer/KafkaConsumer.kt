package com.qpwoei.backend.qpwoei_data.kafka.consumer

interface KafkaConsumer<T> {
    fun receive(messages: List<T>,
                keys: List<String>,
                partitions: List<Int>,
                offsets: List<Long>)
}