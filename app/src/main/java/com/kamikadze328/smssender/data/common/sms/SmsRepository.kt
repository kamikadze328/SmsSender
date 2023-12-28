package com.kamikadze328.smssender.data.common.sms

import androidx.room.withTransaction
import com.kamikadze328.smssender.data.TelegramRepository
import com.kamikadze328.smssender.data.db.AppDatabase
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.model.Sms

class SmsRepository(
    private val smsDbMapper: SmsDbMapper,
    private val telegramRepository: TelegramRepository,
    private val smsToStringConverter: SmsToStringConverter,
    private val database: AppDatabase,
) {
    private suspend fun saveToDb(sms: Sms) {
        val smsDb = smsDbMapper.toDb(sms) ?: return

        database.withTransaction {
            val senderId = database.smsSenderDao().updateOrInsert(smsDb.sender)
            val receiverId = database.smsReceiverDao().updateOrInsert(smsDb.receiver)
            val contentDb = smsDb.info.copy(
                senderId = senderId,
                receiverId = receiverId,
            )
            database.smsDao().insert(contentDb)
        }
    }

    private suspend fun onMessageSuccessfullySent(messageId: Long) {
        database.smsDao().update(messageId, true)
    }

    private suspend fun onMessageSent(isSuccess: Boolean, messageId: Long) {
        if (isSuccess) {
            onMessageSuccessfullySent(messageId)
        }
    }

    suspend fun send(sms: Sms) {
        saveToDb(sms)
        sendAllNotSent()
    }

    private suspend fun sendToTelegram(sms: Sms) {
        val beautifiedSms = smsToStringConverter.convert(sms)
        try {
            val isSuccess = telegramRepository.sendMessage(beautifiedSms)
            onMessageSent(isSuccess, sms.info.messageId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun sendAllNotSent() {
        val notSent = database.smsDao().getAllNotSent()
        notSent.forEach {
            sendToTelegram(smsDbMapper.toDomain(it))
        }
    }
}