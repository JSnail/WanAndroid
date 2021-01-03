package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import kotlinx.coroutines.flow.flow

class LoginRepository constructor(private val api: ApiService) {

    fun login(account: String, password: String) = flow {
        emit(api.login(account, password))
    }
}