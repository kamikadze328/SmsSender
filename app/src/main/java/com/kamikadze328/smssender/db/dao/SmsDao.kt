package com.kamikadze328.smssender.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamikadze328.smssender.db.data.SmsContentDb
import com.kamikadze328.smssender.db.data.SmsDb

@Dao
interface SmsDao {
    @Query("SELECT * FROM SmsContentDb")
    fun getAll(): List<SmsDb>

    @Query("SELECT * FROM SmsContentDb WHERE wasSent = 0")
    fun getAllNotSent(): List<SmsDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sms: SmsContentDb): Long

    @Query("UPDATE SmsContentDb SET wasSent = :wasSent WHERE messageId = :id")
    suspend fun update(id: Long, wasSent: Boolean)

    @Query("SELECT * FROM SmsContentDb ORDER BY dateTime DESC")
    suspend fun getAllSortedByDate(): List<SmsDb>
}