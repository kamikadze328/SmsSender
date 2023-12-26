package com.kamikadze328.smssender.db.data

import androidx.room.Embedded
import androidx.room.Relation

data class SmsDb(
    @Embedded
    val info: SmsContentDb,
    @Relation(
        parentColumn = "receiverId",
        entityColumn = "uid",
    )
    val receiver: SmsReceiverDb,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "uid"
    )
    val sender: SmsSenderDb,
)