package ru.zgrv.divitbot.dto

import ru.zgrv.divitbot.models.User

data class UserData(private val user: User) {
    var id: Long = 0
    var telegramId: Long = 0
    var company: String = ""
    var name: String = ""
    var email: String = ""
    var phone: String = ""

    init {
        id = user.id
        telegramId = user.telegramId
        company = user.company
        name = user.name
        email = user.email
        phone = user.phone
    }

    fun toUser(): User = User().apply {
        this.telegramId = this@UserData.telegramId
        this.company = this@UserData.company
        this.name = this@UserData.name
        this.email = this@UserData.email
        this.phone = this@UserData.phone
    }

}