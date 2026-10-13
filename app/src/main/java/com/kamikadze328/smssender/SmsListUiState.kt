package com.kamikadze328.smssender

import com.kamikadze328.smssender.ui.SmsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SmsListUiState(
    val list: ImmutableList<SmsUi> = persistentListOf(),
    val toastText: String? = null,
    val isLoading: Boolean = false,
)