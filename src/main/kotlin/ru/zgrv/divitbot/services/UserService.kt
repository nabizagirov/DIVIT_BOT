package ru.zgrv.divitbot.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.zgrv.divitbot.models.User
import ru.zgrv.divitbot.repositories.UserRepository
import ru.zgrv.divitbot.utils.Logger


@Service
class UserService(private val repository: UserRepository) : Logger {

    fun findByTelegramId(id: Long): User? {
        try {
            log.info("Поиск пользователя. [telegram_id: $id].")
            val user = repository.findUserByTelegramId(id)
            if (user == null) {
                log.info("Пользователь не найден. [telegram_id: $id]")
                return null
            }
            log.info("Пользователь найден. [telegram_id: $id]")
            return user

        } catch (e: Exception) {
            log.error("Ошибка поиска пользователя. [telegram_id: $id]", e)

        }

        return null
    }

    @Transactional
    fun saveUser(user: User) {
        try {
            findByTelegramId(user.telegramId)?.let {
             return updateUser(it, user)
            }
            log.info("Попытка сохраненя пользователя. [telegram_id: ${user.telegramId}]. [database_id: ${user.id}]")
            repository.save(user)
            log.info("Успешное Сохранение пользователя. [telegram_id: ${user.telegramId}]. [database_id: ${user.id}]")
        } catch (e: Exception) {
            log.error(
                "Ошибка сохранения пользователя. [telegram_id: ${user.telegramId}]. [database_id: ${user.id}]",
                e
            )
        }
    }


    @Transactional
    fun updateUser(old: User, user: User) {

        try {
            log.info("Попытка обновления пользователя. [telegram_id: ${user.telegramId}]. [database_id: ${user.id}]")
            user.apply { id = old.id }
            repository.save(user)
            log.info("Успешное обновление пользователя. [telegram_id: ${user.telegramId}]. database_id: ${user.id}]")
        } catch (e: Exception) {
            log.error(
                "Ошибка обновления пользователя. [telegram_id: ${user.telegramId}]. [database_id: ${user.id}]", e
            )
        }
    }


}