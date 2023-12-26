package com.kamikadze328.smssender.data.common.sms

import android.content.Context
import androidx.room.withTransaction
import com.kamikadze328.smssender.data.TelegramRepository
import com.kamikadze328.smssender.data.db.AppDatabase
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.model.Sms
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder


class SmsRepository(
    private val smsDbMapper: SmsDbMapper,
    private val telegramRepository: TelegramRepository,
) {
    private val format by lazy {
        Json { prettyPrint = true }
    }

    private fun getDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    private suspend fun saveToDb(context: Context, smsDto: Sms) {
        val database = getDatabase(context)
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

    private suspend fun onMessageSent(context: Context, messageId: Long) {
        getDatabase(context).smsDao().update(messageId, true)
    }

    private suspend fun onMessageSent(context: Context, isSuccess: Boolean, messageId: Long) {
        if (isSuccess) {
            onMessageSent(context, messageId)
        }
    }

    suspend fun send(context: Context, smsDto: Sms) {
        saveToDb(context, smsDto)
        sendAllNotSent(context)
    }

    private fun toJsonString(smsDto: Sms): String {
        val json = try {
            format.encodeToString(smsDto)
        } catch (e: SerializationException) {
            e.printStackTrace()
            ""
        }.replace("\"", "")
        return URLEncoder.encode(json, "utf-8")
    }

    private suspend fun sendToTelegram(context: Context, smsDto: Sms) {
        val json = toJsonString(smsDto)

        try {
            val isSuccess = telegramRepository.sendMessage(json)
            onMessageSent(context, isSuccess, smsDto.info.messageId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun sendAllNotSent(context: Context) {
        val database = getDatabase(context)
        val notSent = database.smsDao().getAllNotSent()
        notSent.forEach {
            sendToTelegram(context, smsDbMapper.toDomain(it))
        }
    }
}