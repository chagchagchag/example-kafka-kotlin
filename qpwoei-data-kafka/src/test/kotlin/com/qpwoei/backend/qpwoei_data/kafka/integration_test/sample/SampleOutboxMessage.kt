package com.qpwoei.backend.qpwoei_data.kafka.integration_test.sample

import java.util.*

data class SampleOutboxMessage(
    val name:String,
    val message: String,
    val sagaId: UUID,
)
