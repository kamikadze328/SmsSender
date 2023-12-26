package com.kamikadze328.smssender.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse


class TelegramApi {

    private val client by lazy {
        HttpClient()
    }

    suspend fun sendToTelegram(text: String, botToken: String, chatId: String): HttpResponse {
        val url = "$BASE_LINK/bot$botToken/sendMessage?chat_id=$chatId&text=$text"
        Log.d("kek", "sendToTelegram: text - $text")
        Log.d("kek", "sendToTelegram: url - $url")

        return client.request(url)
    }


    companion object {
        private const val BASE_LINK = "https://api.telegram.org"

        private val instance: TelegramApi by lazy {
            TelegramApi()
        }

        fun instance(): TelegramApi = instance
    }
}