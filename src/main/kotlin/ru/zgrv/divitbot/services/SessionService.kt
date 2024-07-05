package ru.zgrv.divitbot.services

import org.springframework.stereotype.Service
import ru.zgrv.divitbot.bot.Session
import ru.zgrv.divitbot.repositories.SessionRepository
import ru.zgrv.divitbot.utils.Logger

@Service
class SessionService(
    private val repository: SessionRepository
) : Logger {

    fun getSession(telegramId: Long): Session {

        try {
            log.info("Попытка получения сессии для [telegram_id: $telegramId]")
            val session = repository.getSession(telegramId) ?: repository.createSession(telegramId)
            log.info("Успешное получение сессии для [telegram_id: $telegramId]")

            return session
        } catch (e: Exception) {
            log.error("Ошибка при получении сессии для [telegram_id: $telegramId]", e)
            throw e
        }
    }

    fun rebuild(telegramId: Long) = repository.rebuildSession(telegramId)

}