package com.kamikadze328.smssender.domain.sms

import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.kamikadze328.smssender.data.common.ContactsManager
import com.kamikadze328.smssender.model.SmsSenderInfo

class GetSmsSenderUseCase(
    private val contactsManager: ContactsManager,
) {
    fun invoke(context: Context, intent: Intent): SmsSenderInfo {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val possiblePhone = messages.firstOrNull()?.originatingAddress
        val possibleName = contactsManager.getContactName(context, possiblePhone)

        return SmsSenderInfo(
            phone = possiblePhone,
            name = possibleName,
        )
    }
}