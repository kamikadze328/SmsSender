package com.kamikadze328.smssender.domain.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class BatteryDataRepository {
    fun getBatteryLevel(context: Context): BatteryStatus? {
        val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val level = batteryStatus?.let {
            val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level == -1 || scale == -1) return null

            level * 100 / scale.toFloat()
        }?.toInt() ?: return null

        val isPluggedIn = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) > 0

        return BatteryStatus(
            level = level,
            isPluggedIn = isPluggedIn,
        )
    }
}

