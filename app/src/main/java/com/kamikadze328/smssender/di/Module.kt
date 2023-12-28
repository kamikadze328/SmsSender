package com.kamikadze328.smssender.di

import com.kamikadze328.smssender.MainViewModel
import com.kamikadze328.smssender.data.TelegramRepository
import com.kamikadze328.smssender.data.common.ContactsManager
import com.kamikadze328.smssender.data.common.PermissionManager
import com.kamikadze328.smssender.data.common.sms.SmsRepository
import com.kamikadze328.smssender.data.common.sms.SmsToStringConverter
import com.kamikadze328.smssender.data.db.AppDatabase
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.data.network.TelegramApi
import com.kamikadze328.smssender.domain.sms.GetSmsInfoUseCase
import com.kamikadze328.smssender.domain.sms.GetSmsReceiverUseCase
import com.kamikadze328.smssender.domain.sms.GetSmsSenderUseCase
import com.kamikadze328.smssender.domain.sms.ProcessSmsReceivedUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        AppDatabase.buildDatabase(androidContext())
    }
    singleOf(::GetSmsInfoUseCase)
    singleOf(::GetSmsInfoUseCase)
    singleOf(::GetSmsReceiverUseCase)
    singleOf(::GetSmsSenderUseCase)
    singleOf(::ProcessSmsReceivedUseCase)
    singleOf(::SmsRepository)
    singleOf(::ContactsManager)
    singleOf(::PermissionManager)
    singleOf(::TelegramApi)
    singleOf(::TelegramRepository)
    singleOf(::SmsDbMapper)
    singleOf(::SmsToStringConverter)

    viewModelOf(::MainViewModel)
}