package com.checki.home.viewmodels

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.checki.core.data.NetService
import com.checki.core.data.NetServiceDao

/***
 * Repository for interacting with the local database
 */
class NetServiceRepository(private val netServiceDao: NetServiceDao) {

    // Observed LiveData will notify the observer when the data has changed.
    val allNetServices: LiveData<MutableList<NetService>> = netServiceDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(netService: NetService) {
        netServiceDao.insertAll(netService)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(netService: NetService) {
        netServiceDao.delete(netService)
    }
}
