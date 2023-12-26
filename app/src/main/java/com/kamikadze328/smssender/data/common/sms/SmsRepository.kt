package com.kamikadze328.smssender.data.common.sms

import android.content.Context
import androidx.room.withTransaction
import com.kamikadze328.smssender.data.db.AppDatabase
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.data.network.TelegramApi
import com.kamikadze328.smssender.model.Sms
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class SmsRepository(
    private val telegramApi: TelegramApi = TelegramApi.instance(),
    private val smsDbMapper: SmsDbMapper = SmsDbMapper.instance(),
) {
    companion object {
        private val _instance by lazy {
            SmsRepository()
        }

        fun instance(): SmsRepository = _instance
    }

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

    private suspend fun onMessageSent(context: Context, response: HttpResponse, messageId: Long) {
        if (response.status.value in 200..299) {
            onMessageSent(context, messageId)
        }
    }

    suspend fun send(context: Context, smsDto: Sms) {
        saveToDb(context, smsDto)
        sendAllNotSent(context)
    }

    private fun toJsonString(smsDto: Sms): String {
        return try {
            format.encodeToString(smsDto)
        } catch (e: SerializationException) {
            e.printStackTrace()
            ""
        }
            .replace("\n", "%0A")
            .replace("\"", "")
    }

    private suspend fun sendToTelegram(context: Context, smsDto: Sms) {
        val json = toJsonString(smsDto)

        try {
            val response = telegramApi.sendToTelegram(json)
            onMessageSent(context, response, smsDto.info.messageId)
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