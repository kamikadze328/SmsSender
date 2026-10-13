package com.kamikadze328.smssender.domain.battery

data class BatteryStatus(
    val level: Int,
    val isPluggedIn: Boolean,
)