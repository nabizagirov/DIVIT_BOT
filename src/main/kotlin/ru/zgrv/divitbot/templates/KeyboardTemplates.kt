package ru.zgrv.divitbot.templates

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

enum class KeyboardTemplates(val keyboard: List<List<InlineKeyboardButton>>) {
    NEW_USER(listOf(
        listOf(
            InlineKeyboardButton()
                .apply {
                    text = "Заполнить анкету"
                    callbackData = CallbackTemplates.CREATE_PROFILE.callback
                }
        )
    )),

    MENU(
        listOf(
            listOf(
                InlineKeyboardButton().apply {
                    text = "Оставить заявку"
                    callbackData = CallbackTemplates.CREATE_REQUEST.callback
                }),

            listOf(
                InlineKeyboardButton().apply {
                    text = "Ред."
                    callbackData = CallbackTemplates.EDIT_PROFILE.callback

                },
                InlineKeyboardButton().apply {
                    text = "Divit.pro"
                    callbackData = "divit"
                    url = "https://divit.pro/"
                },
            )
        )
    ),

    EDIT_PROFILE(listOf(
        listOf(
            InlineKeyboardButton().apply {
                text = "Отмена"
                callbackData = CallbackTemplates.CANCEL_EDIT_PERSONAL_DATA.callback
            }
        )
    )),

    CANCEL_REQUEST(listOf(
        listOf(
            InlineKeyboardButton().apply {
                text = "Отменить"
                callbackData = CallbackTemplates.CANCEL_REQUEST.callback
            }
        )
    )),

}