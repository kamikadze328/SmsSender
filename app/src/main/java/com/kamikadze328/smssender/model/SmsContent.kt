package com.kamikadze328.smssender.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsContent(
    val messageId: Long,
    val message: String,
    val dateTime: String? = null,
)