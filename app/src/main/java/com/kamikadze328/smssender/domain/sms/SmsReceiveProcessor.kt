package com.kamikadze328.smssender.domain.sms

import android.content.Context
import android.content.Intent
import com.kamikadze328.smssender.domain.data.Sms
import com.kamikadze328.smssender.domain.data.SmsContent
import com.kamikadze328.smssender.domain.data.SmsReceiver
import com.kamikadze328.smssender.domain.data.SmsSenderInfo


class SmsReceiveProcessor(
    private val smsRepository: SmsRepository = SmsRepository.instance(),
    private val smsReceiverResolver: SmsReceiverResolver = SmsReceiverResolver.instance(),
    private val smsInfoResolver: SmsInfoResolver = SmsInfoResolver.instance(),
    private val smsSenderResolver: SmsSenderResolver = SmsSenderResolver.instance(),
) {
    companion object {
        private val instance: SmsReceiveProcessor by lazy {
            SmsReceiveProcessor()
        }

        fun instance(): SmsReceiveProcessor = instance
    }

    suspend fun onReceiveNewSms(context: Context, intent: Intent) {
        val sms = makeSmsDto(context, intent)
        smsRepository.send(context, sms)
    }

    private fun makeSmsDto(context: Context, intent: Intent): Sms {
        return Sms(
            info = getSmsInfo(intent),
            receiver = getSmsReceiverInfo(context, intent),
            sender = getSmsSenderInfo(context, intent),
        )
    }

    private fun getSmsInfo(intent: Intent): SmsContent {
        return smsInfoResolver.getSmsInfo(intent)
    }

    private fun getSmsReceiverInfo(context: Context, intent: Intent): SmsReceiver {
        return smsReceiverResolver.getSmsReceiverInfo(context, intent)
    }

    private fun getSmsSenderInfo(context: Context, intent: Intent): SmsSenderInfo {
        return smsSenderResolver.getSmsSenderInfo(context, intent)
    }


}