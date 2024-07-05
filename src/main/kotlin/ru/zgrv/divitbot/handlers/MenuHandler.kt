package ru.zgrv.divitbot.handlers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.zgrv.divitbot.bot.MessageExecutor
import ru.zgrv.divitbot.bot.Session
import ru.zgrv.divitbot.bot.SessionStage
import ru.zgrv.divitbot.services.SessionService
import ru.zgrv.divitbot.templates.CallbackTemplates
import ru.zgrv.divitbot.templates.KeyboardTemplates

@Controller
class MenuHandler(
    private val messageExecutor: MessageExecutor,
    private val sessionService: SessionService
) : Handler {

    @Value("\${bot.templates.text.start-editing-data}")
    lateinit var startEdit: String

    @Value("\${bot.templates.text.fill-full-name}")
    private lateinit var enterName: String

    @Value("\${bot.templates.text.describe-problem}")
    private lateinit var describeProblem: String


    override suspend fun handle(update: Update): Boolean = when {
        update.hasMessage() && update.message.hasText() -> handleMessage(update.message)
        update.hasCallbackQuery() -> handleCallback(update.callbackQuery)
        else -> false
    }


    private fun handleMessage(message: Message): Boolean {
        val telegramId = message.from.id
        val session = sessionService.getSession(telegramId)
        if (session.stage != SessionStage.AUTH) return false
        val chatId = message.chatId.toString()
        sendMenuText(session, chatId)
        return true
    }

    private fun handleCallback(callback: CallbackQuery): Boolean {
        val telegramId = callback.from.id
        val chatId = callback.message.chatId.toString()
        val session = sessionService.getSession(telegramId)
        val data = callback.data

        if (session.stage != SessionStage.AUTH) return false

        return when (data) {

            CallbackTemplates.EDIT_PROFILE.callback -> {
                messageExecutor.send(startEdit, chatId, KeyboardTemplates.EDIT_PROFILE.keyboard)
                messageExecutor.send(enterName, chatId)
                session.stage = SessionStage.NAME
                true
            }

            CallbackTemplates.CREATE_REQUEST.callback -> {
                messageExecutor.send(describeProblem, chatId, KeyboardTemplates.CANCEL_REQUEST.keyboard)
                session.stage = SessionStage.REQUEST
                true
            }

            else -> false
        }

    }


    private fun sendMenuText(session: Session, chatId: String) {
        messageExecutor.send(session.mainMenuText(), chatId, KeyboardTemplates.MENU.keyboard)
    }

}