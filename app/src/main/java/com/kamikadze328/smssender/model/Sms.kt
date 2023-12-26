package com.kamikadze328.smssender.model

import kotlinx.serialization.Serializable

@Serializable
data class Sms(
    val info: SmsContent,
    val receiver: SmsReceiver,
    val sender: SmsSenderInfo,
)