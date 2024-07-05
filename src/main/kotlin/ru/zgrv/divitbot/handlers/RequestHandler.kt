package ru.zgrv.divitbot.handlers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.zgrv.divitbot.bot.MessageExecutor
import ru.zgrv.divitbot.bot.Session
import ru.zgrv.divitbot.bot.SessionStage
import ru.zgrv.divitbot.services.EmailService
import ru.zgrv.divitbot.services.SessionService
import ru.zgrv.divitbot.templates.CallbackTemplates
import ru.zgrv.divitbot.templates.KeyboardTemplates

@Component
class RequestHandler(
    private val sessionService: SessionService,
    private val messageExecutor: MessageExecutor,
    private val emailService: EmailService,
) : Handler {

    @Value("\${bot.templates.text.request-processing}")
    private lateinit var beforeRequestSend: String

    @Value("\${bot.templates.text.request-processed}")
    private lateinit var afterRequestSend: String

    override suspend fun handle(update: Update): Boolean = when {
        update.hasMessage() && update.message.hasText() -> handleMessage(update.message)
        update.hasCallbackQuery() -> handleCallback(update.callbackQuery)
        else -> false
    }


    private fun handleMessage(message: Message): Boolean {
        val telegramId = message.from.id
        val session = sessionService.getSession(telegramId)

        if (session.stage != SessionStage.REQUEST) return false

        val chatId = message.chatId.toString()
        val requestText = message.text

        messageExecutor.send(beforeRequestSend, chatId)

        emailService.sendTextEmail(
            "${session.user.company}. ${session.user.name}",
            """
                ${session.requestUserInfo()}
Текст заявки:
$requestText
            """.trimIndent()
        )

        messageExecutor.send(afterRequestSend, chatId)
        session.stage = SessionStage.AUTH

        return true
    }

    private fun handleCallback(callback: CallbackQuery): Boolean {
        val telegramId = callback.from.id
        var session = sessionService.getSession(telegramId)

        if (session.stage != SessionStage.REQUEST) return false
        val chatId = callback.message.chatId.toString()
        val data = callback.data

        return when (data) {
            CallbackTemplates.CANCEL_REQUEST.callback -> {
                session = sessionService.rebuild(telegramId)
                sendMenuText(session, chatId)
                true
            }

            else -> false
        }
    }

    private fun sendMenuText(session: Session, chatId: String) {
        messageExecutor.send(session.mainMenuText(), chatId, KeyboardTemplates.MENU.keyboard)
    }

}