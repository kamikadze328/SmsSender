package com.kamikadze328.smssender.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kamikadze328.smssender.data.db.model.SmsContentDb
import com.kamikadze328.smssender.data.db.model.SmsDb

@Dao
interface SmsDao {
    @Transaction
    @Query("SELECT * FROM SmsContentDb")
    fun getAll(): List<SmsDb>

    @Transaction
    @Query("SELECT * FROM SmsContentDb WHERE wasSent = 0")
    fun getAllNotSent(): List<SmsDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sms: SmsContentDb): Long

    @Transaction
    @Query("UPDATE SmsContentDb SET wasSent = :wasSent WHERE messageId = :id")
    suspend fun update(id: Long, wasSent: Boolean)

    @Transaction
    @Query("SELECT * FROM SmsContentDb ORDER BY dateTime DESC")
    suspend fun getAllSortedByDate(): List<SmsDb>
}