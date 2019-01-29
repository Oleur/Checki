package com.checki.home.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.checki.core.data.CheckiDatabase
import com.checki.core.data.NetService
import com.checki.core.network.await
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all services.
 */
class NetServiceViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    // OkHttp instance
    private val okHttpClient by lazy { OkHttpClient() }

    // Repository
    private val repository: NetServiceRepository

    // LiveData to be observed to notify the UI
    val allNetServices: LiveData<List<NetService>>

    init {
        val checkiDao = CheckiDatabase.getDatabase(application).netServiceDao()
        repository = NetServiceRepository(checkiDao)
        allNetServices = repository.allNetServices
    }

    /**
     * Insert or update a NetService in the database
     * @param netService NetService to insert or update
     */
    fun insert(netService: NetService) = scope.launch(Dispatchers.IO) {
        repository.insert(netService)
    }

    /**
     * Delete a given NetService in the database
     * @param netService NetService to delete
     */
    fun delete(netService: NetService) = scope.launch(Dispatchers.IO) {
        repository.delete(netService)
    }

    /**
     * Ping all services to get their status
     * @param checkTime timestamp
     */
    fun pingAllServices(checkTime: Long) = scope.launch(Dispatchers.IO) {
        allNetServices.value?.forEach {
            val request = Request.Builder()
                .url(it.url)
                .build()
            try {
                // Launch the okhttp request asynchronously
                val response =  okHttpClient.newCall(request).await()

                // Update the service with the new response code and check time
                it.status = response.code()
                it.lastCheckedAt = checkTime

                // Update the service in the database
                repository.insert(it)
            } catch (ex: Exception) {
                // Ignore cancel exception
                ex.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
