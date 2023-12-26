package com.kamikadze328.smssender.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kamikadze328.smssender.db.dao.SmsDao
import com.kamikadze328.smssender.db.dao.SmsReceiverDao
import com.kamikadze328.smssender.db.dao.SmsSenderDao
import com.kamikadze328.smssender.db.data.SmsContentDb
import com.kamikadze328.smssender.db.data.SmsReceiverDb
import com.kamikadze328.smssender.db.data.SmsSenderDb

@Database(
    entities = [SmsSenderDb::class, SmsReceiverDb::class, SmsContentDb::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "database-name"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

    abstract fun smsDao(): SmsDao
    abstract fun smsSenderDao(): SmsSenderDao
    abstract fun smsReceiverDao(): SmsReceiverDao
}