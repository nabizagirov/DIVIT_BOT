package ru.zgrv.divitbot.repositories

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import ru.zgrv.divitbot.bot.Session
import ru.zgrv.divitbot.models.User
import ru.zgrv.divitbot.services.UserService
import ru.zgrv.divitbot.utils.Logger

@Repository
class SessionRepository(
    private val userService: UserService,
) : Logger {

    private val sessions: MutableMap<Long, Session> = mutableMapOf()


    fun getSession(id: Long): Session? {
        return sessions[id]
    }

    fun createSession(id: Long): Session {

        return Session(userService.findByTelegramId(id) ?: User()).also {
            sessions[id] = it
            log.info("Сессия для [telegram_id: $id] создана")
        }
    }

    fun removeSession(id: Long) {
        sessions.remove(id)
        log.info("Сессия для [telegram_id: $id] удалена")
    }

    fun rebuildSession(id: Long): Session {
        removeSession(id)
        return createSession(id)
    }

    @Scheduled(fixedDelay = 60000)
    private fun checkExpiredSessions() {
        val expirationTime = 10 * 60000
        val currentTime = System.currentTimeMillis()
        val expiredSessions = sessions.filterKeys {
            (currentTime - sessions[it]!!.lastTimeActivity.time) >= expirationTime
        }

        expiredSessions.forEach { removeSession(it.key) }
    }

}