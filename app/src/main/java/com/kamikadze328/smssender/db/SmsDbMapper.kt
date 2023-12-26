package com.kamikadze328.smssender.db

import com.kamikadze328.smssender.db.data.SmsContentDb
import com.kamikadze328.smssender.db.data.SmsDb
import com.kamikadze328.smssender.db.data.SmsReceiverDb
import com.kamikadze328.smssender.db.data.SmsSenderDb
import com.kamikadze328.smssender.domain.data.Sms
import com.kamikadze328.smssender.domain.data.SmsContent
import com.kamikadze328.smssender.domain.data.SmsReceiver
import com.kamikadze328.smssender.domain.data.SmsSenderInfo
import java.util.Date

class SmsDbMapper {

    companion object {
        private val instance: SmsDbMapper by lazy {
            SmsDbMapper()
        }

        fun instance(): SmsDbMapper = instance
    }

    fun toDb(sms: Sms): SmsDb? {
        return SmsDb(
            info = sms.info.toDb(),
            receiver = sms.receiver.toDb() ?: return null,
            sender = sms.sender.toDb() ?: return null,
        )
    }

    private fun SmsReceiver.toDb(): SmsReceiverDb? {
        return SmsReceiverDb(
            phone = phone?.toString() ?: return null,
            simSlot = simSlot,
            cardId = cardId,
            displayName = displayName?.toString(),
            contact = contact,
        )
    }

    private fun SmsSenderInfo.toDb(): SmsSenderDb? {
        phone ?: return null
        return SmsSenderDb(
            name = name,
            phone = phone,
        )
    }

    private fun SmsContent.toDb(): SmsContentDb {
        return SmsContentDb(
            messageId = messageId,
            message = message,
            dateTime = Date()
        )
    }

    fun toDomain(smsDb: SmsDb): Sms {
        return Sms(
            info = smsDb.info.toDomain(),
            receiver = smsDb.receiver.toDomain(),
            sender = smsDb.sender.toDomain(),
        )
    }

    private fun SmsSenderDb.toDomain(): SmsSenderInfo {
        return SmsSenderInfo(
            name = name,
            phone = phone,
        )
    }
    private fun SmsReceiverDb.toDomain(): SmsReceiver {
        return SmsReceiver(
            phone = phone,
            simSlot = simSlot,
            cardId = cardId,
            displayName = displayName,
            contact = contact,
        )
    }
    private fun SmsContentDb.toDomain(): SmsContent {
        return SmsContent(
            messageId = messageId,
            message = message,
            dateTime = dateTime.toString(),
        )
    }
}