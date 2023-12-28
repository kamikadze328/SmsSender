package com.kamikadze328.smssender.data.common.sms

import android.content.Context
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.model.Sms

class SmsToStringConverter(
    private val applicationContext: Context,
) {
    companion object {
        private const val INBOX_TRAY_EMOJI = "\uD83D\uDCE4"
        private const val POSTBOX_EMOJI = "\uD83D\uDCEE"
        private const val CALENDAR_EMOJI = "\uD83D\uDCC6"
    }

    fun convert(sms: Sms): String {
        val senderTitle = applicationContext.getString(R.string.sms_sender)
        val senderInfo = makeSenderString(sms)
        val receiverTitle = applicationContext.getString(R.string.sms_receiver)
        val receiverInfo = makeReceiverString(sms)
        val dateTimeTitle = applicationContext.getString(R.string.sms_sent_when)
        val dateTime = makeDateTimeString(sms)
        val message = sms.info.message

        return buildString {
            appendLine("$INBOX_TRAY_EMOJI <b>$senderTitle</b> $senderInfo")
            appendLine("$POSTBOX_EMOJI <b>$receiverTitle</b> $receiverInfo")
            appendLine("$CALENDAR_EMOJI <b>$dateTimeTitle</b> $dateTime")
            appendLine("___________________________________")
            appendLine()
            append(message)
        }
    }

    private fun makeSenderString(sms: Sms): String {
        val name = sms.sender.name
        val phone = sms.sender.phone
        return when {
            !name.isNullOrBlank() && !phone.isNullOrBlank() -> "$name (${phone})"
            !phone.isNullOrBlank() -> phone
            !name.isNullOrBlank() -> name
            else -> applicationContext.getString(R.string.sms_sender_unknown)
        }
    }

    private fun makeReceiverString(sms: Sms): String {
        val name = sms.receiver.displayName
        val phone = sms.receiver.phone
        return when {
            !name.isNullOrBlank() && !phone.isNullOrBlank() -> "$name (${phone})"
            !phone.isNullOrBlank() -> phone
            !name.isNullOrBlank() -> name
            else -> applicationContext.getString(R.string.sms_receiver_unknown)
        }
    }

    private fun makeDateTimeString(sms: Sms): String {
        return sms.info.dateTime.toString()
    }
}