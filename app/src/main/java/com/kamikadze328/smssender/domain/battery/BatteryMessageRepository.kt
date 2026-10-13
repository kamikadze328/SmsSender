package com.kamikadze328.smssender.domain.battery

import android.content.Context
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.data.TelegramRepository

class BatteryMessageRepository(
    private val telegramRepository: TelegramRepository,
    private val context: Context,
) {
    companion object {
        const val EMOJI_BATTERY_LOW = "\uD83E\uDEAB"
    }

    suspend fun sendBatteryLowMessage(batteryLevel: Int) {
        val message = buildString {
            append(EMOJI_BATTERY_LOW)
            append(" ")
            appendLine(context.getString(R.string.battery_low, batteryLevel))
            appendLine()
            appendLine(context.getString(R.string.charge_device))
        }
        telegramRepository.sendMessage(message)
    }
}