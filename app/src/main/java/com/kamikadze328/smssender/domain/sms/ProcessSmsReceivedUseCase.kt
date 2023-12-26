package com.kamikadze328.smssender.domain.sms

import android.content.Context
import android.content.Intent
import com.kamikadze328.smssender.data.common.sms.SmsRepository
import com.kamikadze328.smssender.model.Sms
import com.kamikadze328.smssender.model.SmsContent
import com.kamikadze328.smssender.model.SmsReceiver
import com.kamikadze328.smssender.model.SmsSenderInfo


class ProcessSmsReceivedUseCase(
    private val smsRepository: SmsRepository,
    private val getSmsReceiverUseCase: GetSmsReceiverUseCase,
    private val getSmsInfoUseCase: GetSmsInfoUseCase,
    private val getSmsSenderUseCase: GetSmsSenderUseCase,
) {
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
        return getSmsInfoUseCase.invoke(intent)
    }

    private fun getSmsReceiverInfo(context: Context, intent: Intent): SmsReceiver {
        return getSmsReceiverUseCase.invoke(context, intent)
    }

    private fun getSmsSenderInfo(context: Context, intent: Intent): SmsSenderInfo {
        return getSmsSenderUseCase.invoke(context, intent)
    }


}