package com.kamikadze328.smssender.model

data class SmsReceiver(
    val phone: String?,
    val simSlot: Int?,
    val cardId: Int?,
    val displayName: String?,
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