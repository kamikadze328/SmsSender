package com.kamikadze328.smssender.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsReceiver(
    val phone: CharSequence?,
    val simSlot: Int?,
    val cardId: Int?,
    val displayName: CharSequence?,
    val contact: String?,
) {
    companion object {
        const val UNKNOWN_VALUE = -1
        val EMPTY = SmsReceiver(
            phone = null,
            simSlot = null,
            cardId = null,
            displayName = null,
            contact = null,
        )
    }
}