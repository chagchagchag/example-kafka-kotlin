package com.qpwoei.backend.qpwoei_data.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LezhinDataKafkaApplication

fun main(args: Array<String>){
    runApplication<LezhinDataKafkaApplication>(*args)
}