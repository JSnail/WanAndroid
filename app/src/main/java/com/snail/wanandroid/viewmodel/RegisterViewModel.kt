package com.snail.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.repository.RegisterRepository
import kotlinx.coroutines.flow.flow

class RegisterViewModel constructor(private val registerRepository: RegisterRepository) :
    BaseViewModel() {

    fun startRegister(account: String, password: String) {

    }


}