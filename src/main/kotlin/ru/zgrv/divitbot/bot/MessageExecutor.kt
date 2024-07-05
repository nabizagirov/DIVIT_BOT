package ru.zgrv.divitbot.bot

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


@Component(value = "executor")
class MessageExecutor(@Lazy private val bot: TelegramLongPollingBot) {

    companion object {

        fun sendMessageBuilder(
            text: String,
            chatId: String,
            keyboard: List<List<InlineKeyboardButton>> = emptyList()
        ): SendMessage = SendMessage().apply {
            enableMarkdownV2(true)
            this.text = escapeMarkdownV2(text)
            this.chatId = chatId
            replyMarkup = InlineKeyboardMarkup().apply { this.keyboard = keyboard }
        }

        fun editMessageTextBuilder(
            text: String,
            chatId: String,
            messageId: Int,
            keyboard: List<List<InlineKeyboardButton>> = emptyList()
        ): EditMessageText = EditMessageText(chatId).apply {
            enableMarkdown(true)
            this.messageId = messageId
            this.text = escapeMarkdownV2(text)
            replyMarkup = InlineKeyboardMarkup().apply { this.keyboard = keyboard }
        }

        private fun escapeMarkdownV2(text: String): String {
            val specialChars = hashSetOf(
                '_', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!'
            )

            val escapedText = StringBuilder()
            for (char in text) {
                if (specialChars.contains(char)) {
                    escapedText.append("\\$char")
                } else {
                    escapedText.append(char)
                }
            }
            return escapedText.toString()
        }
    }

    fun send(
        text: String,
        chatId: String,
        keyboard: List<List<InlineKeyboardButton>> = emptyList()
    ) {
        bot.execute(sendMessageBuilder(text, chatId, keyboard))
    }

    fun edit(
        text: String,
        chatId: String,
        messageId: Int,
        keyboard: List<List<InlineKeyboardButton>> = emptyList()
    ) {
        bot.execute(editMessageTextBuilder(text, chatId, messageId, keyboard))
    }

}
