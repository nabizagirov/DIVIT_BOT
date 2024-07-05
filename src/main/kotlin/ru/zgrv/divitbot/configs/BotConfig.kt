package ru.zgrv.divitbot.configs

import kotlinx.coroutines.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.zgrv.divitbot.handlers.MainHandler

@Configuration
class BotConfig {

    @Bean
    fun telegramBotsApi(bot: TelegramLongPollingBot): TelegramBotsApi =
        TelegramBotsApi(DefaultBotSession::class.java).apply { registerBot(bot) }

    @Bean(name = ["bot"])
    fun longPollingBotInstance(properties: BotProperties, handler: MainHandler): TelegramLongPollingBot {
        return object : TelegramLongPollingBot(properties.token) {

            private val coroutineScope = CoroutineScope(Dispatchers.Default)
            override fun getBotUsername() = properties.username

            override fun onUpdateReceived(update: Update?) {
                if (update == null) return
                coroutineScope.launch { handler.handle(update) }
            }
        }
    }

}