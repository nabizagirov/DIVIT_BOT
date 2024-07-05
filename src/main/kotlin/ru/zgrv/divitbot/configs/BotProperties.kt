package ru.zgrv.divitbot.configs

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bot.config")
class BotProperties(val username: String, val token: String) {
}