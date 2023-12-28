package com.kamikadze328.smssender.data

import com.kamikadze328.smssender.BuildConfig
import com.kamikadze328.smssender.data.common.SettingsRepository
import com.kamikadze328.smssender.data.network.TelegramApi

class TelegramRepository(
    private val telegramApi: TelegramApi,
    private val settingsRepository: SettingsRepository,
) {
    private fun getToken(): String = BuildConfig.TG_BOT_ID

    private fun getChatId(): String = BuildConfig.TG_CHAT_ID

    suspend fun sendMessage(text: String): Boolean {
        return try {
            telegramApi.sendToTelegram(
                text = text,
                botToken = getToken(),
                chatId = getChatId(),
                shouldSendMessageSilent = settingsRepository.shouldSendMessageSilent(),
            )
            true
        } catch (e: Exception) {
            false
        }
    }
}