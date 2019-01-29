package com.checki.core.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NetServiceDao {
    @Query("SELECT * FROM netservice ORDER BY last_checked_at DESC")
    fun getAll(): LiveData<List<NetService>>

    @Query("SELECT * FROM netservice WHERE name LIKE :nameToFind LIMIT 1")
    fun findByName(nameToFind: String): NetService

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg netServices: NetService)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(vararg netServices: NetService)

    @Delete
    fun delete(netService: NetService)
}