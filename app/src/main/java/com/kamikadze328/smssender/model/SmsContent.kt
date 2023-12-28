package com.kamikadze328.smssender.model

import java.util.Date

data class SmsContent(
    val messageId: Long,
    val message: String,
    val dateTime: Date = Date(),
    val wasSent: Boolean = false,
)