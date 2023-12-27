package com.kamikadze328.smssender.data

import com.kamikadze328.smssender.BuildConfig
import com.kamikadze328.smssender.data.network.TelegramApi

class TelegramRepository(
    private val telegramApi: TelegramApi,
) {
    private fun getToken(): String = BuildConfig.TG_BOT_ID

    private fun getChatId(): String = BuildConfig.TG_CHAT_ID

    suspend fun sendMessage(text: String): Boolean {
        val response = telegramApi.sendToTelegram(
            text = text,
            botToken = getToken(),
            chatId = getChatId(),
        )
        return response.status.value in 200..299
    }
}