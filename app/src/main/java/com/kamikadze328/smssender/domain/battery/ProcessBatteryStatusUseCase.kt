package com.kamikadze328.smssender.domain.battery

import android.content.Context

class ProcessBatteryStatusUseCase(
    private val messageRepository: BatteryMessageRepository,
    private val batteryDataRepository: BatteryDataRepository,
) {
    companion object {
        private const val LOW_BATTERY_THRESHOLD = 10
    }

    suspend fun onReceiveBatteryLevel(context: Context) {
        val batteryStatus = batteryDataRepository.getBatteryLevel(context) ?: return

        if (batteryStatus.isPluggedIn) {
            return
        }

        if (batteryStatus.level <= LOW_BATTERY_THRESHOLD) {
            messageRepository.sendBatteryLowMessage(batteryStatus.level)
        }
    }

}