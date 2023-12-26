package com.kamikadze328.smssender.db.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = SmsSenderDb::class,
            childColumns = ["senderId"],
            parentColumns = ["uid"],
            onUpdate = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SmsReceiverDb::class,
            childColumns = ["receiverId"],
            parentColumns = ["uid"],
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["senderId"]),
        Index(value = ["receiverId"]),
        Index(value = ["messageId", "receiverId", "senderId"])
    ]
)
data class SmsContentDb(
    @PrimaryKey
    val messageId: Long,
    val message: String,
    val dateTime: Date,
    val wasSent: Boolean = false,
    val senderId: Long = 0,
    val receiverId: Long = 0,
)