package com.kamikadze328.smssender.domain.common

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager() {
    companion object {
        const val MY_PERMISSIONS_REQUEST_SMS = 101

        private val _instance by lazy {
            PermissionManager()
        }

        fun instance(): PermissionManager = _instance
    }

    fun areAllPermissionsGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.all {
            val permission = ContextCompat.checkSelfPermission(context, it)
            permission == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkPermissionAndRequest(activity: Activity, permissions: List<String>): List<String>? {
        val nonGrantedPermissions = permissions.filter {
            val permission = ContextCompat.checkSelfPermission(activity, it)
            permission != PackageManager.PERMISSION_GRANTED
        }.ifEmpty {
            return null
        }

        ActivityCompat.requestPermissions(
            activity,
            nonGrantedPermissions.toTypedArray(),
            MY_PERMISSIONS_REQUEST_SMS
        )
        return nonGrantedPermissions
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS) {
            val areAllPermissionsGranted = grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            if (areAllPermissionsGranted) {
                onSuccess()
            } else {
                onError()
            }
        }
    }
}
