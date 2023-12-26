package com.kamikadze328.smssender.domain.sms

import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.kamikadze328.smssender.domain.common.ContactsManager
import com.kamikadze328.smssender.domain.data.SmsSenderInfo

class SmsSenderResolver(
    private val contactsManager: ContactsManager = ContactsManager.instance(),
) {
    companion object {
        private val instance: SmsSenderResolver by lazy {
            SmsSenderResolver()
        }

        fun instance(): SmsSenderResolver = instance
    }

    fun getSmsSenderInfo(context: Context, intent: Intent): SmsSenderInfo {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val possiblePhone = messages.firstOrNull()?.originatingAddress
        val possibleName = contactsManager.getContactName(context, possiblePhone)

        return SmsSenderInfo(
            phone = possiblePhone,
            name = possibleName,
        )
    }
}