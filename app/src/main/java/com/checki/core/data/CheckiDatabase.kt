package com.checki.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database to store all the user entered
 * Database available thanks to a singleton.
 */
@Database(entities = [NetService::class], version = 1)
abstract class CheckiDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: CheckiDatabase? = null

        private const val DB_NAME = "checki_database"

        fun getDatabase(context: Context): CheckiDatabase {
            // if the INSTANCE is not null, then return it, if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CheckiDatabase::class.java,
                    DB_NAME).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    abstract fun netServiceDao(): NetServiceDao
}