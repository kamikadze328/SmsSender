package com.kamikadze328.smssender.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kamikadze328.smssender.db.data.SmsSenderDb

@Dao
interface SmsSenderDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insert(sender: SmsSenderDb): Long

    @Query("SELECT * FROM SmsSenderDb WHERE phone = :phone")
    suspend fun getByPhone(phone: String): SmsSenderDb?

    @Update
    suspend fun update(sender: SmsSenderDb)

    @Transaction
    suspend fun updateOrInsert(sender: SmsSenderDb): Long {
        val cachedSender = getByPhone(sender.phone)
        return if (cachedSender == null) {
            insert(sender)
        } else {
            val cachedUid = cachedSender.uid
            update(sender.copy(uid = cachedUid))
            cachedUid
        }
    }
}