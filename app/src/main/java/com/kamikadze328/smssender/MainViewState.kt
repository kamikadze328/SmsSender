package com.kamikadze328.smssender

import com.kamikadze328.smssender.ui.SmsList

data class MainViewState(
    val sms: SmsList = SmsList(),
    val toastText: String? = null,
    val showForegroundService: Boolean = false,
)