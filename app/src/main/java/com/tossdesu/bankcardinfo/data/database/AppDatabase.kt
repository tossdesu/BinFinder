package com.tossdesu.bankcardinfo.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tossdesu.bankcardinfo.data.database.entity.CardBinDbEntity

@Database(
    entities = [CardBinDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cardBinsDao(): CardBinsDao

    companion object {

        private const val DB_NAME = "binds_finder.db"
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}