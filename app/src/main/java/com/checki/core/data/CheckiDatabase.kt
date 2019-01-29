package com.checki.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [NetService::class], version = 1)
abstract class CheckiDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: CheckiDatabase? = null

        fun getDatabase(context: Context): CheckiDatabase {
            // if the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CheckiDatabase::class.java,
                    "checki_database.db").build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    abstract fun netServiceDao(): NetServiceDao
}