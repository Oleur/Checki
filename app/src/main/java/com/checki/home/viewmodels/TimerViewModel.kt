package com.checki.home.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * A ViewModel used for refreshing the data every minute
 */
class TimerViewModel : ViewModel() {

    companion object {
        private const val ONE_MINUTE = 60 * 1000L
        private const val ONE_SECOND = 1000L
    }

    val liveDataCheckTime = MutableLiveData<Long>()
    val liveDataTime = MutableLiveData<Long>()

    init {
        // Update services every second
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                liveDataCheckTime.postValue(System.currentTimeMillis())
            }
        }, ONE_MINUTE, ONE_MINUTE)

        // Task for updating the list every second
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                liveDataTime.postValue(System.currentTimeMillis())
            }
        }, ONE_SECOND, ONE_SECOND)
    }

}