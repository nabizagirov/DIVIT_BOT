package ru.zgrv.divitbot.handlers


import org.telegram.telegrambots.meta.api.objects.Update

interface Handler {

    suspend fun handle(update: Update): Boolean
}