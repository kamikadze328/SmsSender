package com.kamikadze328.smssender.ui

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SmsUi(
    val receiverName: String,
    val senderName: String,
    val text: String,
    val isSent: Boolean,
    val dateTime: String,
)

@Immutable
data class SmsList(
    val list: ImmutableList<SmsUi> = persistentListOf(),
)