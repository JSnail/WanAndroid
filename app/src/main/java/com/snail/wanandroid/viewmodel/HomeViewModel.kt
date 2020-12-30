package com.snail.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.repository.HomeRepository

class HomeViewModel constructor(private val homeRepository: HomeRepository) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}