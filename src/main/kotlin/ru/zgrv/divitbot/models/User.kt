package ru.zgrv.divitbot.models

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0

    @Column(name = "telegram_id")
    var telegramId: Long = 0

    @Column(name = "name")
    var name: String = ""

    @Column(name = "company")
    var company: String = ""

    @Column(name = "phone")
    var phone: String = ""

    @Column(name = "email")
    var email: String = ""

}