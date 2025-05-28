package com.kamikadze328.smssender

import com.kamikadze328.smssender.ui.SmsList

data class SmsListUiState(
    val sms: SmsList = SmsList(),
    val toastText: String? = null,
    val isLoading: Boolean = false,
)