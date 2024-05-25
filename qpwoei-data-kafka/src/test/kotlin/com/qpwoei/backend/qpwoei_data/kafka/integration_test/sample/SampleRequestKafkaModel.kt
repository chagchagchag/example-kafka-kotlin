package com.qpwoei.backend.qpwoei_data.kafka.integration_test.sample

import java.io.Serializable
import java.util.*

data class SampleRequestKafkaModel(
    val id: UUID,
    val message: String,
    val sagaId: UUID,
) : Serializable
