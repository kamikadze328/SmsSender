package com.kamikadze328.smssender.domain

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
import com.kamikadze328.smssender.domain.common.PermissionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainViewModel(
    private val permissionManager: PermissionManager = PermissionManager.instance(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainViewState()
    )
    val uiState: StateFlow<MainViewState> = _uiState.asStateFlow()

    private var permissionsRequestCode: List<String>? = null
    private var permissionRequestCount: Int = 0


    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        activity: Activity,
    ) {
        permissionManager.onRequestPermissionsResult(
            requestCode = requestCode,
            permissions = permissions,
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

    fun checkAndRequestPermissions(activity: Activity) {
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

    fun foregroundServiceStarted() {
        _uiState.update {
            it.copy(
                showForegroundService = false,
            )
        }
    }

    fun onToastShown() {
        _uiState.update {
            it.copy(
                toastText = null,
            )
        }
    }

    fun onPermissionsRequested() {
        _uiState.update {
            it.copy(
                checkPermissions = false,
            )
        }
    }
}

data class MainViewState(
    val sms: List<String> = emptyList(),
    val toastText: String? = null,
    val showForegroundService: Boolean = false,
    val checkPermissions: Boolean = true,
)