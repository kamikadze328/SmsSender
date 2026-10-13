package com.kamikadze328.smssender.data.common.sms

import android.content.Context
import android.telephony.PhoneNumberUtils
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.model.Sms

class SmsToStringConverter(
    private val context: Context,
) {
    companion object {
        private const val EMOJI_INBOX_TRAY = "\uD83D\uDCE4"
        private const val EMOJI_POSTBOX = "\uD83D\uDCEE"
        private const val EMOJI_CALENDAR = "\uD83D\uDCC6"
    }

    fun convert(sms: Sms): String {
        val senderTitle = context.getString(R.string.sms_sender)
        val senderInfo = makeSenderString(sms)
        val receiverTitle = context.getString(R.string.sms_receiver)
        val receiverInfo = makeReceiverString(sms)
        val dateTimeTitle = context.getString(R.string.sms_sent_when)
        val dateTime = makeDateTimeString(sms)
        val message = sms.info.message

        return buildString {
            appendLine("$EMOJI_INBOX_TRAY <b>$senderTitle</b> $senderInfo")
            appendLine("$EMOJI_POSTBOX <b>$receiverTitle</b> $receiverInfo")
            appendLine("$EMOJI_CALENDAR <b>$dateTimeTitle</b> $dateTime")
            appendLine("___________________________________")
            appendLine()
            append(message)
        }
    }

    fun makeSenderString(sms: Sms, isShort: Boolean = false): String {
        val name = sms.sender.name
        val phone = formatPhone(sms.sender.phone)
        return when {
            !name.isNullOrBlank() && !phone.isNullOrBlank() && !isShort -> "$name (${phone})"
            !name.isNullOrBlank() && !phone.isNullOrBlank() -> name

            !phone.isNullOrBlank() -> phone
            !name.isNullOrBlank() -> name
            else -> context.getString(R.string.sms_sender_unknown)
        }
    }

    fun makeReceiverString(sms: Sms, isShort: Boolean = false): String {
        val name = sms.receiver.displayName
        val phone = formatPhone(sms.receiver.phone)
        val isPhoneEqualsName = name == phone
        return when {
            !name.isNullOrBlank() && !phone.isNullOrBlank() && !isShort && !isPhoneEqualsName -> "$name (${phone})"
            !name.isNullOrBlank() && !phone.isNullOrBlank() -> name
            !phone.isNullOrBlank() -> phone
            !name.isNullOrBlank() -> name
            else -> context.getString(R.string.sms_receiver_unknown)
        }
    }

    fun makeDateTimeString(sms: Sms): String {
        return sms.info.dateTime.toString()
    }

    private fun formatPhone(phone: String?): String? {
        if (phone.isNullOrBlank()) {
            return null
        }

        return if (PhoneNumberUtils.isGlobalPhoneNumber(phone)) {
            PhoneNumberUtils.formatNumber(phone, "RU")
        } else {
            phone
        }

    }
}