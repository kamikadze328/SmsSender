package com.kamikadze328.smssender.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsSenderInfo(
    val name: String?,
    val phone: String?,
)