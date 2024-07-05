package ru.zgrv.divitbot.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.zgrv.divitbot.models.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByTelegramId(id: Long) : User?
}