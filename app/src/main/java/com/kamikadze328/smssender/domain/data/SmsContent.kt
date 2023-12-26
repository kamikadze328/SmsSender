package com.kamikadze328.smssender.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class SmsContent(
    val messageId: Long,
    val message: String,
    val dateTime: String? = null,
)