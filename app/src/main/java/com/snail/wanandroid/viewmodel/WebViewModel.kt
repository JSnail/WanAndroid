package com.snail.wanandroid.viewmodel

import androidx.lifecycle.viewModelScope
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.repository.WebRepository
import kotlinx.coroutines.launch

class WebViewModel (private val repository: WebRepository):BaseViewModel() {

    fun collect(id: Int) {
        viewModelScope.launch(handlerExpectation) {
            repository.collect(id)
        }
    }

    fun unCollect(id: Int) {
        viewModelScope.launch(handlerExpectation) {
            repository.unCollect(id)
        }
    }
}