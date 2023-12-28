package com.kamikadze328.smssender.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramRequestDto(
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("text")
    val text: String,
    @SerialName("disable_notification")
    val disableNotification: Boolean = false,
    @SerialName("parse_mode")
    val parseMode: String = "HTML",
)