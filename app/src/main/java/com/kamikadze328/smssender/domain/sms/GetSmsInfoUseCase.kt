package com.kamikadze328.smssender.domain.sms

import android.content.Intent
import android.provider.Telephony
import com.kamikadze328.smssender.model.SmsContent

class GetSmsInfoUseCase {
    companion object {
        private val instance: GetSmsInfoUseCase by lazy {
            GetSmsInfoUseCase()
        }

        fun instance(): GetSmsInfoUseCase = instance
    }

    fun invoke(intent: Intent): SmsContent {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val smsBody = buildString {
            messages.forEach {
                append(it.messageBody)
            }
        }
        return SmsContent(
            messageId = getMessageId(intent),
            message = smsBody,
        )
    }

    private fun getMessageId(intent: Intent): Long {
        return intent.getLongExtra("messageId", -1L)
    }
}