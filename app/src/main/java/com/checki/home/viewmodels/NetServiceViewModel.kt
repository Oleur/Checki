package com.checki.home.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.checki.core.data.CheckiDatabase
import com.checki.core.data.NetService
import com.checki.core.network.await
import com.checki.core.network.isOnline
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.CoroutineContext

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all services.
 */
class NetServiceViewModel constructor(application: Application) : AndroidViewModel(application) {

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
    val allNetServices: LiveData<MutableList<NetService>>

    val onlineLiveData: MutableLiveData<Boolean>

    init {
        val checkiDao = CheckiDatabase.getDatabase(application).netServiceDao()
        repository = NetServiceRepository(checkiDao)
        allNetServices = repository.allNetServices
        onlineLiveData = MutableLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    //region Public methods

    /**
     * Insert or update a NetService in the database
     * @param netService NetService to insert or update
     */
    fun insert(netService: NetService) = scope.launch(Dispatchers.IO) {
        repository.insert(netService)

        // Ping network only if we are connected
        if (getApplication<Application>().applicationContext.isOnline()) {
            pingService(netService, System.currentTimeMillis())
        }

        // TODO: Handle network changes to notify the user that services cannot be pinged
    }

    /**
     * Delete a given NetService in the database
     * @param netService NetService to delete
     */
    fun delete(netService: NetService?) = scope.launch(Dispatchers.IO) {
        netService?.let { repository.delete(netService) }
    }

    /**
     * Ping all services to get their status
     * @param checkTime timestamp
     */
    fun pingAllServices(checkTime: Long) = scope.launch(Dispatchers.IO) {
        // Ping network only if we are connected
        if (getApplication<Application>().applicationContext.isOnline()) {
            allNetServices.value?.forEach {
                pingService(it, checkTime)
            }
        }

        // TODO: Handle network changes to notify the user that services cannot be pinged
    }

    //endregion

    //region Internal methods

    private suspend fun pingService(netService: NetService, checkTime: Long) {
        val request = Request.Builder()
            .url(netService.url)
            .build()
        try {
            // Launch the okhttp request asynchronously
            val response =  okHttpClient.newCall(request).await()

            // Update the service with the new response code and check time
            netService.status = response.code()
            netService.lastCheckedAt = checkTime
        } catch (ex: Exception) {
            // Unknown Host save it as 404 status code
            netService.status = 404
            netService.lastCheckedAt = checkTime
        } finally {
            // Update the service in the database
            repository.insert(netService)
        }
    }

    //endregion
}
