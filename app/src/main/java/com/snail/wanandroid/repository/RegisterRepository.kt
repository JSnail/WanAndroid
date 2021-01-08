package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import kotlinx.coroutines.flow.flow

class RegisterRepository constructor(private val api: ApiService) {

    fun startRegister(account: String, password: String) = flow {
        emit(api.register(account, password, password))
    }
}