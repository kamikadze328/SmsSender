package com.kamikadze328.smssender.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.kamikadze328.smssender.domain.sms.ProcessSmsReceivedUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SmsBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val processor: ProcessSmsReceivedUseCase by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            goAsync {
                processor.onReceiveNewSms(context, intent)
            }
        }
    }
}