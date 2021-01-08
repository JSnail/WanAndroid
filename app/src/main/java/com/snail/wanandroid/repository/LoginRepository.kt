package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.db.AppDataBase
import com.snail.wanandroid.entity.UserEntity
import kotlinx.coroutines.flow.flow

class LoginRepository constructor(private val api: ApiService, private val dataBase: AppDataBase) {

    fun login(account: String, password: String) = flow {
        emit(api.login(account, password))
    }

    suspend fun saveUser(userEntity: UserEntity) {
        dataBase.userDao().insertUser(userEntity)
    }
}