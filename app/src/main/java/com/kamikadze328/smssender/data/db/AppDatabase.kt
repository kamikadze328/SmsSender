package com.kamikadze328.smssender.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kamikadze328.smssender.data.db.dao.SmsDao
import com.kamikadze328.smssender.data.db.dao.SmsReceiverDao
import com.kamikadze328.smssender.data.db.dao.SmsSenderDao
import com.kamikadze328.smssender.data.db.model.SmsContentDb
import com.kamikadze328.smssender.data.db.model.SmsReceiverDb
import com.kamikadze328.smssender.data.db.model.SmsSenderDb

@Database(
    entities = [SmsSenderDb::class, SmsReceiverDb::class, SmsContentDb::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "database-name"

        fun buildDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }

    abstract fun smsDao(): SmsDao
    abstract fun smsSenderDao(): SmsSenderDao
    abstract fun smsReceiverDao(): SmsReceiverDao
}