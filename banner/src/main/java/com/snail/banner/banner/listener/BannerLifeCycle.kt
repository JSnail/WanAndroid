package com.snail.banner.banner.listener

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.snail.banner.banner.BannerConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BannerLifeCycle constructor(
    private val onBannerLifeListener: OnBannerLifeListener,
    private val dataSize: Int
    ) : LifecycleObserver {
    private var mStartLoop = false
    private var tempPosition = dataSize

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
    }

    private fun startBannerLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            countDown()
        }
    }

    private suspend fun countDown() {
        delay(BannerConfig.BANNER_LOOP_TIME)
        if (mStartLoop) {
            val position = calculatePosition(tempPosition)
            onBannerLifeListener.onNext(position)
            countDown()
        }
    }

    private fun calculatePosition(position: Int): Int {
        return if (position == dataSize) {
            tempPosition =0
            tempPosition
        } else {
            tempPosition += 1
            tempPosition
        }
    }
}