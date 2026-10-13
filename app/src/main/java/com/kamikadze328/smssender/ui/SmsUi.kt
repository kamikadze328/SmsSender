package com.kamikadze328.smssender.ui

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SmsUi(
    val id: Long,
    val receiverName: String,
    val senderName: String,
    val text: String,
    val isSent: Boolean,
    val dateTime: String,
)