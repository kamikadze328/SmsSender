package com.kamikadze328.smssender.domain.network

import android.util.Log
import com.kamikadze328.smssender.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse


class TelegramApi {

    private val client by lazy {
        HttpClient()
    }

    suspend fun sendToTelegram(text: String): HttpResponse {
        Log.d("kek", "sendToTelegram: $text")
        val (botToken, chatId) = getChatIdAndBotToken()
        val url = "$BASE_LINK/bot$botToken/sendMessage?chat_id=$chatId&text=$text"

        return client.request(url)
    }


    companion object {
        private const val BASE_LINK = "https://api.telegram.org"
        fun getChatIdAndBotToken(): Pair<String, String> {
            return BuildConfig.TG_BOT_ID to BuildConfig.TG_CHAT_ID
        }

        private val instance: TelegramApi by lazy {
            TelegramApi()
        }

        fun instance(): TelegramApi = instance
    }
}