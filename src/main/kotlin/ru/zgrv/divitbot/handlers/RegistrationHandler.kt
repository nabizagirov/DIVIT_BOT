package ru.zgrv.divitbot.handlers

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.zgrv.divitbot.bot.MessageExecutor
import ru.zgrv.divitbot.bot.Session
import ru.zgrv.divitbot.bot.SessionStage
import ru.zgrv.divitbot.services.SessionService
import ru.zgrv.divitbot.services.UserService
import ru.zgrv.divitbot.templates.CallbackTemplates
import ru.zgrv.divitbot.templates.KeyboardTemplates

@Component
class RegistrationHandler(
    private val userService: UserService,
    private val sessionService: SessionService,
    private val messageExecutor: MessageExecutor,
) : Handler {

    @Value("\${bot.templates.text.new-user-message}")
    private lateinit var initMessage: String

    @Value("\${bot.templates.text.press-button-to-start-filling-questionnaire}")
    private lateinit var startButton: String

    @Value("\${bot.templates.text.fill-full-name}")
    private lateinit var enterName: String

    @Value("\${bot.templates.text.fill-company}")
    private lateinit var enterCompany: String

    @Value("\${bot.templates.text.fill-phone}")
    private lateinit var enterPhone: String

    @Value("\${bot.templates.text.fill-email}")
    private lateinit var enterEmail: String

    @Value("\${bot.templates.text.stop-editing-data}")
    private lateinit var cancelEditing: String

    @Value("\${bot.templates.text.start-editing-data}")
    lateinit var startEdit: String


    override suspend fun handle(update: Update): Boolean = when {
        update.hasMessage() && update.message.hasText() -> handleMessage(update.message)
        update.hasCallbackQuery() -> handleCallback(update.callbackQuery)
        else -> false
    }


    private fun handleMessage(message: Message): Boolean {
        val telegramId = message.from.id
        val chatId = message.chatId.toString()
        val session = sessionService.getSession(telegramId)

        when (session.stage) {

            SessionStage.NOT_AUTH -> {
                messageExecutor.send(initMessage, chatId)
                messageExecutor.send(startButton, chatId, KeyboardTemplates.NEW_USER.keyboard)
                return true
            }

            SessionStage.NAME -> {
                messageExecutor.send(enterCompany, chatId)
                session.user.name = message.text
                session.stage = SessionStage.COMPANY
                return true
            }

            SessionStage.COMPANY -> {
                messageExecutor.send(enterPhone, chatId)
                session.user.company = message.text
                session.stage = SessionStage.PHONE
                return true
            }

            SessionStage.PHONE -> {
                messageExecutor.send(enterEmail, chatId)
                session.user.phone = message.text
                session.stage = SessionStage.EMAIL
                return true
            }

            SessionStage.EMAIL -> {
                session.user.email = message.text
                session.user.telegramId = telegramId
                userService.saveUser(session.user)
                sessionService.rebuild(telegramId)

                sendMenuText(session, chatId)
                return true
            }

            SessionStage.AUTH -> {
                sendMenuText(session,chatId)
                return true
            }

            else -> return false
        }

    }

    private fun handleCallback(callback: CallbackQuery): Boolean {
        val telegramId = callback.from.id
        val chatId = callback.message.chatId.toString()
        var session = sessionService.getSession(telegramId)
        val data = callback.data

        return when (session.stage) {
            SessionStage.NOT_AUTH -> {
                if (data != CallbackTemplates.CREATE_PROFILE.callback) return false
                messageExecutor.send(enterName, chatId)
                session.stage = SessionStage.NAME
                true
            }

            SessionStage.AUTH -> {
                if (data != CallbackTemplates.EDIT_PROFILE.callback) return false
                messageExecutor.send(startEdit, chatId, KeyboardTemplates.EDIT_PROFILE.keyboard)
                messageExecutor.send(enterName, chatId)
                session.stage = SessionStage.NAME
                true
            }

            else -> {
                if (session.user.telegramId.toInt() == 0) return false
                if (data == CallbackTemplates.CANCEL_EDIT_PERSONAL_DATA.callback) {
                    messageExecutor.send(cancelEditing, chatId)
                    session = sessionService.rebuild(telegramId)
                    sendMenuText(session, chatId)
                    return true
                }

                false
            }
        }

    }



    private fun sendMenuText(session: Session, chatId: String) {
        messageExecutor.send(session.mainMenuText(), chatId, KeyboardTemplates.MENU.keyboard)
    }


}