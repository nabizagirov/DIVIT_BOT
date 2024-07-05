package ru.zgrv.divitbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class DivitBotApplication

fun main(args: Array<String>) {
    runApplication<DivitBotApplication>(*args)
}
