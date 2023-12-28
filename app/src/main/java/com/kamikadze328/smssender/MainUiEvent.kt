package com.kamikadze328.smssender

import android.app.Activity

sealed interface MainUiEvent {
    data class OnPermissionsResult(
        val requestCode: Int,
        val grantResults: List<Int>,
        val activity: Activity,
    ) : MainUiEvent
    data object OnToastShown : MainUiEvent
    data object ForegroundServiceStarted : MainUiEvent
    data class OnInit(
        val activity: Activity,
    ) : MainUiEvent

    data object OnRefreshClicked : MainUiEvent
}