package com.snail.wanandroid.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class LoadingViewLiveData : MutableLiveData<Boolean>() {
    private val status: AtomicBoolean = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in Boolean>) {
        super.observe(owner, {
            if (status.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: Boolean?) {
        status.set(true)
        super.setValue(value)
    }


    @MainThread
    fun clean() {
        value = null
    }
}