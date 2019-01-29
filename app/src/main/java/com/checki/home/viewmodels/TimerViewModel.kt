package com.checki.home.viewmodels

import androidx.lifecycle.LiveData
import android.os.SystemClock
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

    private val mInitialTime: Long = SystemClock.elapsedRealtime()

    val elapsedTime: LiveData<Long>
        get() = liveDataElapsedTime

    init {
        val timer = Timer()

        // Update the elapsed time every second.
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000L
                liveDataElapsedTime.postValue(newValue)
            }
        }, ONE_MINUTE, ONE_MINUTE)
    }

}