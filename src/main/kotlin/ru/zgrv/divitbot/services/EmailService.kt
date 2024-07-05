package ru.zgrv.divitbot.services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val sender: JavaMailSender
) {
    private val senderEmail = "sp13@divit.pro"
    private val defaultReceiverEmail = "help@divit.pro"
    fun sendTextEmail(title: String, body: String, to: String = defaultReceiverEmail) {
        val message = SimpleMailMessage()
        message.from = senderEmail
        message.setTo(to)
        message.subject = title
        message.text = body
        sender.send(message)
    }
}