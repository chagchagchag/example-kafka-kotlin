package com.qpwoei.backend.qpwoei_data.kafka.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "kafka-consumer-config")
data class KafkaConsumerProperties(
    var keyDeserializer: String,
    var valueDeserializer: String,
    var reviewConsumerGroupId: String,
    var statisticsConsumerGroupId: String,
    var usersConsumerGroupId: String,
    var autoOffsetReset: String,
    var specificAvroReaderKey: String,
    var specificAvroReader: String,
    var batchListener: Boolean,
    var autoStartup: Boolean,
    var concurrencyLevel: Int,
    var sessionTimeoutMs: Int,
    var heartbeatIntervalMs: Int,
    var maxPollIntervalMs: Int,
    var maxPollRecords: Int,
    var maxPartitionFetchBytesDefault: Int,
    var maxPartitionFetchBytesBoostFactor: Int,
    var pollTimeoutMs: Long,

)
