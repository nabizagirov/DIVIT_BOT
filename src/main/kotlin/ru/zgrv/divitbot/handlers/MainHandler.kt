package ru.zgrv.divitbot.handlers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class MainHandler(
    private val handlers: List<Handler>,
) {
    suspend fun handle(update: Update) = withContext(Dispatchers.Default) {

        for (handler in handlers) {
            if(handler.handle(update)) return@withContext
        }
    }

}