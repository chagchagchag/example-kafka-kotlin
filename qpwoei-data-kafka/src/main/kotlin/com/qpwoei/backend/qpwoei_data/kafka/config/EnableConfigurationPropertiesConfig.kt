package com.qpwoei.backend.qpwoei_data.kafka.config

import com.qpwoei.backend.qpwoei_data.kafka.properties.KafkaConsumerProperties
import com.qpwoei.backend.qpwoei_data.kafka.properties.KafkaProducerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    KafkaConsumerProperties::class,
    KafkaProducerProperties::class,
)
class EnableConfigurationPropertiesConfig {
}