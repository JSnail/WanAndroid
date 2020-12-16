package com.snail.wanandroid.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {
    protected val dialogLiveData =MutableLiveData<Boolean>()
}