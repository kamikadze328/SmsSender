package com.kamikadze328.smssender.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class TelegramApi(
    private val client: HttpClient,
) {

    suspend fun sendToTelegram(
        text: String,
        botToken: String,
        chatId: String,
        shouldSendMessageSilent: Boolean,
    ) {
        Log.d("kek", "sendToTelegram: text - $text")
        client.post("$BASE_LINK/bot$botToken/sendMessage") {
            setBody(
                TelegramRequestDto(
                    chatId = chatId,
                    text = text,
                    disableNotification = shouldSendMessageSilent,
                )
            )
        }
    }


    companion object {
        private const val BASE_LINK = "https://api.telegram.org"
    }
}

