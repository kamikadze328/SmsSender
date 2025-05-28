package com.kamikadze328.smssender

import android.app.Activity

sealed interface SmsListUiEvent {
    data class OnPermissionsResult(
        val requestCode: Int,
        val grantResults: List<Int>,
        val activity: Activity,
    ) : SmsListUiEvent

    data object OnToastShown : SmsListUiEvent
    data class OnInit(
        val activity: Activity,
    ) : SmsListUiEvent

    data object OnRefreshClicked : SmsListUiEvent
}