package com.checki.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

/**
 * A ViewModel used for refreshing the data every minute
 */
class TimerViewModel : ViewModel() {

    companion object {
        private const val ONE_MINUTE = 60 * 1000L
    }

    private val liveDataElapsedTime = MutableLiveData<Long>()

    val checkTime: LiveData<Long>
        get() = liveDataElapsedTime

    init {
        // Update services every second
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                liveDataElapsedTime.postValue(System.currentTimeMillis())
            }
        }, ONE_MINUTE, ONE_MINUTE)
    }

}