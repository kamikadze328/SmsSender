package com.kamikadze328.smssender.data.common.sms

import androidx.room.withTransaction
import com.kamikadze328.smssender.data.TelegramRepository
import com.kamikadze328.smssender.data.db.AppDatabase
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.model.Sms
import java.net.URLEncoder

class SmsRepository(
    private val smsDbMapper: SmsDbMapper,
    private val telegramRepository: TelegramRepository,
    private val database: AppDatabase,
) {
    private suspend fun saveToDb(smsDto: Sms) {
        val smsDb = smsDbMapper.toDb(smsDto) ?: return

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

    suspend fun send(smsDto: Sms) {
        saveToDb(smsDto)
        sendAllNotSent()
    }

    private fun beautifySms(smsDto: Sms): String {
        val built = buildString {
            append("Отправитель: ${smsDto.sender.name} (${smsDto.sender.phone})\n")
            append("Получатель: ${smsDto.receiver.displayName} (${smsDto.receiver.phone})\n")
            append("Когда: ${smsDto.info.dateTime}\n")
            append("-------------------------------------\n\n")
            append(smsDto.info.message)
        }
        return URLEncoder.encode(built, "utf-8")
    }

    private suspend fun sendToTelegram(smsDto: Sms) {
        val beautifiedSms = beautifySms(smsDto)
        try {
            val isSuccess = telegramRepository.sendMessage(beautifiedSms)
            onMessageSent(isSuccess, smsDto.info.messageId)
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