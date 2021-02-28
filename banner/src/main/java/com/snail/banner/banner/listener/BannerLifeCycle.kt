package com.snail.banner.banner.listener

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*

class BannerLifeCycle constructor(
    private val onBannerLifeListener: OnBannerLifeListener,
    private val dataSize: Int,
    private val intervalTime: Long
) : LifecycleObserver {
    private var mStartLoop = false
    private var tempPosition = dataSize-1
    private var job: Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.i("TAG", "BannerLifeCycle   onResume")
        mStartLoop = true
        startBannerLoop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        Log.i("TAG", "BannerLifeCycle   pause")
        mStartLoop = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        Log.i("TAG", "BannerLifeCycle   destroy")
        mStartLoop = false
        job?.cancel()
    }

    private fun startBannerLoop() {
        if (null == job) {
            job = CoroutineScope(Dispatchers.IO).launch {
                countDown()
            }
        }
    }

    private suspend fun countDown() {
        if (mStartLoop) {
            delay(intervalTime)
            val position = calculatePosition(tempPosition)
            withContext(Dispatchers.Main) {
                onBannerLifeListener.onNext(position)
            }
            countDown()
        }
    }

    private fun calculatePosition(position: Int): Int {
        return if (position == dataSize-1) {
            tempPosition = 0
            tempPosition
        } else {
            tempPosition += 1
            tempPosition
        }
    }
}