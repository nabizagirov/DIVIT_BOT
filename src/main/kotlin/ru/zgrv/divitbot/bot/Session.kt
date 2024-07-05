package ru.zgrv.divitbot.bot

import ru.zgrv.divitbot.models.User
import java.sql.Timestamp


class Session(var user: User) {

    var lastTimeActivity: Timestamp = Timestamp(System.currentTimeMillis())
    var stage: SessionStage = if (this.user.id.toInt() == 0) SessionStage.NOT_AUTH else SessionStage.AUTH


    fun mainMenuText(): String = """
        📑 *Ваши данные*
        
        • ФИО: ${user.name}
        • Номер телефона: ${user.phone}
        • Почта: ${user.email}
        • Компания: ${user.company}
    """.trimIndent()

    fun requestUserInfo() : String = """
        Данные пользователя
        
        • ФИО: ${user.name}
        • Номер телефона: ${user.phone}
        • Почта: ${user.email}
        • Компания: ${user.company}
        ---------------------------
    """.trimIndent()
}


enum class SessionStage {
    AUTH, NOT_AUTH, NAME, COMPANY, PHONE, EMAIL, REQUEST
}