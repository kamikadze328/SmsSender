package com.kamikadze328.smssender.data.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["phone"], unique = true)])
data class SmsSenderDb(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    val phone: String,
    val name: String?,
)