package com.kamikadze328.smssender.domain.common

import android.Manifest
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.PhoneLookup
import android.util.Log


class ContactsManager(
    private val permissionManager: PermissionManager = PermissionManager.instance(),
) {
    companion object {

        private val _instance by lazy {
            ContactsManager()
        }

        fun instance(): ContactsManager = _instance
    }

    fun getContactName(context: Context, phoneNumber: String?): String? {
        Log.d("kek", "getContactName: $phoneNumber")
        if (phoneNumber.isNullOrBlank()) return null
        if (!permissionManager.areAllPermissionsGranted(
                context,
                listOf(Manifest.permission.READ_CONTACTS)
            )
        ) {
            return null
        }
        val cursor = context.contentResolver.query(
            Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)),
            arrayOf(PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        ) ?: return null
        var contactName: String? = null
        if (cursor.moveToFirst()) {
            val columnName = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)
            if (columnName != -1) {
                contactName = cursor.getString(columnName)
            }
        }
        if (!cursor.isClosed) {
            cursor.close()
        }
        return contactName
    }
}