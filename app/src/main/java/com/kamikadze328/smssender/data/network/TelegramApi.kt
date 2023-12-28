package com.kamikadze328.smssender.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


class TelegramApi {

    private val client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor =>", message)
                    }

                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("HTTP status:", "${response.status.value}")
                }
            }

            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }

    }

    suspend fun sendToTelegram(text: String, botToken: String, chatId: String): HttpResponse {
        Log.d("kek", "sendToTelegram: text - $text")
        return client.post {
            url("$BASE_LINK/bot$botToken/sendMessage")
            setBody(
                TelegramRequest(
                    chatId = chatId,
                    text = text,
                    entities = listOf(
                        TelegramMessageEntity(
                            offset = 0,
                            length = 10,
                        )
                    )
                )
            )
        }
        //return client.request(url)
    }


    companion object {
        private const val BASE_LINK = "https://api.telegram.org"
    }
}

@Serializable
data class TelegramRequest(
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("text")
    val text: String,
    @SerialName("parse_mode")
    val parseMode: String = "HTML",
    @SerialName("entities")
    val entities: List<TelegramMessageEntity>? = null,
)

@Serializable
data class TelegramMessageEntity(
    @SerialName("type")
    val type: String = "bold",
    @SerialName("offset")
    val offset: Int,
    @SerialName("length")
    val length: Int,
)
