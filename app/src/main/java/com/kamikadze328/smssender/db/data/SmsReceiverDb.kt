package com.kamikadze328.smssender.db.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["phone"], unique = true)]
)
data class SmsReceiverDb(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val phone: String,
    val simSlot: Int?,
    val cardId: Int?,
    val displayName: String?,
    val contact: String?,
)