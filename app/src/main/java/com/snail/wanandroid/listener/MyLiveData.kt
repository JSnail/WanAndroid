package com.snail.wanandroid.listener

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class MyLiveData<T> : MutableLiveData<T>() {

    fun myObserve(key: String, clas: Class<T>, owner: LifecycleOwner, observer: Observer<in T>){
        val cls = clas.declaredFields
        cls.forEach {
            if (key == it.name) {
                observe(owner,observer)
            }
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
    }

}