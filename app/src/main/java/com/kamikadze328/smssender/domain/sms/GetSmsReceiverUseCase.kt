package com.kamikadze328.smssender.domain.sms

import android.Manifest.permission.READ_PHONE_NUMBERS
import android.Manifest.permission.READ_PHONE_STATE
import android.Manifest.permission.READ_SMS
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.kamikadze328.smssender.data.common.ContactsManager
import com.kamikadze328.smssender.data.common.PermissionManager
import com.kamikadze328.smssender.model.SmsReceiver

class GetSmsReceiverUseCase(
    private val permissionManager: PermissionManager,
    private val contactsManager: ContactsManager,
) {
    companion object {
        private val SIM_INFO_POSSIBLE_EXTRA_NAMES = listOf(
            "simSlot",
            "phone",
            "slot",
            "slotId",
            "slot_id",
            "slotIdx",
            "simId",
            "simSlot",
            "simnum",
            "android.telephony.extra.SLOT_INDEX"
        )

        private val SUBSCRIPTION_ID_POSSIBLE_EXTRA_NAMES = listOf(
            "android.telephony.extra.SUBSCRIPTION_INDEX", "subscription"
        )
    }

    fun invoke(context: Context, intent: Intent): SmsReceiver {
        if (!hasPermissionsForGettingPhoneNumber(context)) return SmsReceiver.EMPTY

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            makeSmsReceiverInfo(context, intent)
        } else {
            makeSmsReceiverInfoOld(context, intent)
        } ?: SmsReceiver.EMPTY
    }


    private fun getSimSlot(intent: Intent): Int {
        SIM_INFO_POSSIBLE_EXTRA_NAMES.forEach {
            val simInfo = intent.getIntExtra(it, SmsReceiver.UNKNOWN_VALUE)
            if (simInfo != SmsReceiver.UNKNOWN_VALUE) {
                return simInfo
            }
        }
        return SmsReceiver.UNKNOWN_VALUE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getSubscriptionManager(context: Context): SubscriptionManager? {
        return context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as? SubscriptionManager
    }

    private fun getTelephonyManager(context: Context): TelephonyManager? {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
    }

    private fun getSubscriptionId(intent: Intent): Int {
        SUBSCRIPTION_ID_POSSIBLE_EXTRA_NAMES.forEach {
            val subscriptionId = intent.getIntExtra(it, SmsReceiver.UNKNOWN_VALUE)
            if (subscriptionId != SmsReceiver.UNKNOWN_VALUE) {
                return subscriptionId
            }
        }
        return SmsReceiver.UNKNOWN_VALUE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getSubscriptionInfo(
        subscriptionManager: SubscriptionManager,
        subIndex: Int,
    ): SubscriptionInfo? {
        return try {
            subscriptionManager.getActiveSubscriptionInfo(subIndex)
        } catch (e: SecurityException) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun makeSmsReceiverInfo(context: Context, intent: Intent): SmsReceiver? {
        val subscriptionManager = getSubscriptionManager(context) ?: return null
        return try {
            val subscriptionId = getSubscriptionId(intent)
            val subInfo = getSubscriptionInfo(subscriptionManager, subscriptionId)

            val phone1 = subscriptionManager.getPhoneNumber(subscriptionId)
            val phone2 = subInfo?.displayName

            @Suppress("DEPRECATION")
            val phone3 = subInfo?.number

            val phone = when {
                phone1.isNotBlank() -> phone1
                !phone2.isNullOrBlank() -> phone2.toString()
                !phone3.isNullOrBlank() -> phone3
                else -> null
            }

            val name = subInfo?.displayName?.toString()
            SmsReceiver(
                phone = phone,
                simSlot = subInfo?.simSlotIndex ?: getSimSlot(intent),
                cardId = subInfo?.cardId,
                displayName = name,
                contact = getContactName(context, phone, name),
            )
        } catch (e: SecurityException) {
            null
        }
    }

    private fun getContactName(
        context: Context,
        phone: CharSequence?,
        name: CharSequence?
    ): String? {
        val contact1 = contactsManager.getContactName(context, phone?.toString())
        val contact2 = contactsManager.getContactName(context, name?.toString())
        return when {
            !contact1.isNullOrBlank() -> contact1
            !contact2.isNullOrBlank() -> contact2
            else -> null
        }
    }

    @SuppressLint("HardwareIds")
    private fun makeSmsReceiverInfoOld(context: Context, intent: Intent): SmsReceiver? {
        val telephonyManager = getTelephonyManager(context) ?: return null
        return try {
            @Suppress("DEPRECATION")
            val phone = telephonyManager.line1Number
            val contactName = contactsManager.getContactName(context, phone)
            SmsReceiver.EMPTY.copy(
                phone = phone,
                simSlot = getSimSlot(intent),
                displayName = contactName,
            )
        } catch (e: SecurityException) {
            null
        }
    }

    private fun hasPermissionsForGettingPhoneNumber(context: Context): Boolean {
        return permissionManager.areAllPermissionsGranted(
            context,
            buildList {
                add(READ_SMS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    add(READ_PHONE_NUMBERS)
                }
                add(READ_PHONE_STATE)
            }
        )
    }
}