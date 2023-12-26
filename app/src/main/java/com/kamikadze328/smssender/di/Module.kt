package com.kamikadze328.smssender.di

import com.kamikadze328.smssender.MainViewModel
import com.kamikadze328.smssender.data.TelegramRepository
import com.kamikadze328.smssender.data.common.ContactsManager
import com.kamikadze328.smssender.data.common.PermissionManager
import com.kamikadze328.smssender.data.common.sms.SmsRepository
import com.kamikadze328.smssender.data.db.mapper.SmsDbMapper
import com.kamikadze328.smssender.data.network.TelegramApi
import com.kamikadze328.smssender.domain.sms.GetSmsInfoUseCase
import com.kamikadze328.smssender.domain.sms.GetSmsReceiverUseCase
import com.kamikadze328.smssender.domain.sms.GetSmsSenderUseCase
import com.kamikadze328.smssender.domain.sms.ProcessSmsReceivedUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { GetSmsInfoUseCase() }
    single { GetSmsReceiverUseCase(get(), get()) }
    single { GetSmsSenderUseCase(get()) }
    single { ProcessSmsReceivedUseCase(get(), get(), get(), get()) }
    single { SmsRepository(get(), get()) }
    single { ContactsManager(get()) }
    single { PermissionManager() }
    single { TelegramApi() }
    single { TelegramRepository() }
    single { SmsDbMapper() }

    viewModelOf(::MainViewModel)
}