package com.kamikadze328.smssender.model

data class Sms(
    val info: SmsContent,
    val receiver: SmsReceiver,
    val sender: SmsSenderInfo,
    val wasSent: Boolean = false,
)