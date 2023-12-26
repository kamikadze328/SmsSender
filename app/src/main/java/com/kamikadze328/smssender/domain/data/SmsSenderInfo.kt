package com.kamikadze328.smssender.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class SmsSenderInfo(
    val name: String?,
    val phone: String?,
)