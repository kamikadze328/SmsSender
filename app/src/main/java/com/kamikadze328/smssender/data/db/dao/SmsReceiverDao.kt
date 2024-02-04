package com.kamikadze328.smssender.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kamikadze328.smssender.data.db.model.SmsReceiverDb

@Dao
interface SmsReceiverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receiverDb: SmsReceiverDb): Long

    @Query("SELECT * FROM SmsReceiverDb WHERE phone = :phone")
    suspend fun getByPhone(phone: String): SmsReceiverDb?

    @Query("SELECT * FROM SmsReceiverDb WHERE simSlot = :simSlot")
    suspend fun getBySimSlot(simSlot: Int): SmsReceiverDb?

    @Update
    suspend fun update(receiverDb: SmsReceiverDb)

    @Transaction
    suspend fun updateOrInsert(receiver: SmsReceiverDb): Long {
        val cachedReceiver = getByPhone(receiver.phone)
            ?: receiver.simSlot?.let { getBySimSlot(it) }
        return if (cachedReceiver == null) {
            insert(receiver)
        } else {
            val cachedUid = cachedReceiver.uid
            update(receiver.copy(uid = cachedUid))
            cachedUid
        }
    }
}