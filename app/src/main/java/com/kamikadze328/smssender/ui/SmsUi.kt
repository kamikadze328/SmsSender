package com.kamikadze328.smssender.ui

import androidx.compose.runtime.Stable

@Stable
data class SmsUi(
    val receiverName: String,
    val senderName: String,
    val text: String,
    val isSent: Boolean,
    val dateTime: String,
)

@Stable
data class SmsList(
    val list: List<SmsUi> = emptyList(),
)