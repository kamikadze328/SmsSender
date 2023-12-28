package com.kamikadze328.smssender

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kamikadze328.smssender.data.common.PermissionManager
import com.kamikadze328.smssender.data.common.sms.SmsRepository
import com.kamikadze328.smssender.data.common.sms.SmsToStringConverter
import com.kamikadze328.smssender.model.Sms
import com.kamikadze328.smssender.ui.SmsList
import com.kamikadze328.smssender.ui.SmsUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel(
    private val permissionManager: PermissionManager,
    private val smsToStringConverter: SmsToStringConverter,
    private val smsRepository: SmsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainViewState()
    )
    val uiState: StateFlow<MainViewState> = _uiState.asStateFlow()

    private var permissionsRequestCode: List<String>? = null
    private var permissionRequestCount: Int = 0

    private fun onInit(event: MainUiEvent.OnInit) {
        checkAndRequestPermissions(event.activity)
        updateSmsList()
    }

    private fun updateSmsList() {
        viewModelScope.launch(Dispatchers.IO) {
            val sms = smsRepository.getAll()
            val smsUi = sms.map { it.toUi() }
            _uiState.update {
                it.copy(
                    sms = SmsList(smsUi),
                )
            }
        }
    }

    private fun Sms.toUi(): SmsUi {
        return SmsUi(
            receiverName = smsToStringConverter.makeReceiverString(this, true),
            senderName = smsToStringConverter.makeSenderString(this, true),
            text = info.message,
            isSent = info.wasSent,
            dateTime = smsToStringConverter.makeDateTimeString(this),
        )
    }

    fun onEvent(event: MainUiEvent) {
        when (event) {
            is MainUiEvent.OnToastShown -> onToastShown()
            is MainUiEvent.ForegroundServiceStarted -> foregroundServiceStarted()
            is MainUiEvent.OnInit -> onInit(event)
            is MainUiEvent.OnRefreshClicked -> onRefreshClicked()
            is MainUiEvent.OnPermissionsResult -> onRequestPermissionsResult(
                requestCode = event.requestCode,
                grantResults = event.grantResults.toIntArray(),
                activity = event.activity,
            )
        }
    }

    private fun onRefreshClicked() {
        updateSmsList()
    }

    private fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        activity: Activity,
    ) {
        permissionManager.onRequestPermissionsResult(
            requestCode = requestCode,
            grantResults = grantResults,
            onSuccess = {
                startForegroundService()
            },
            onError = {
                showToast("You must grand all permissions to work application fine")
                checkAndRequestPermissions(activity)
            }
        )
    }

    private fun checkAndRequestPermissions(activity: Activity) {
        permissionRequestCount++
        if (permissionRequestCount > 3) {
            navigateToSettings(activity)
        }

        permissionsRequestCode = requestPermission(activity)
        if (permissionsRequestCode == null) {
            startForegroundService()
        }
    }

    private fun requestPermission(activity: Activity): List<String>? {
        val permissions = buildList {
            add(Manifest.permission.READ_SMS)
            add(Manifest.permission.RECEIVE_SMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                add(Manifest.permission.READ_PHONE_NUMBERS)
            }
            add(Manifest.permission.READ_PHONE_STATE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            add(Manifest.permission.READ_CONTACTS)
        }
        return permissionManager.checkPermissionAndRequest(activity, permissions)
    }

    private fun navigateToSettings(context: Context) {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        with(intent) {
            data = Uri.fromParts("package", context.packageName, null)
            addCategory(CATEGORY_DEFAULT)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
            addFlags(FLAG_ACTIVITY_NO_HISTORY)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }

    private fun startForegroundService() {
        _uiState.update {
            it.copy(
                showForegroundService = true,
            )
        }
    }

    private fun showToast(text: String) {
        _uiState.update {
            it.copy(
                toastText = text,
            )
        }
    }

    private fun foregroundServiceStarted() {
        _uiState.update {
            it.copy(
                showForegroundService = false,
            )
        }
    }

    private fun onToastShown() {
        _uiState.update {
            it.copy(
                toastText = null,
            )
        }
    }
}