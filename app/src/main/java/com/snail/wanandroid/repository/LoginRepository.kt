package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.db.AppDataBase
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.utils.SharePreferencesUtils
import kotlinx.coroutines.flow.flow

class LoginRepository constructor(private val api: ApiService, private val dataBase: AppDataBase) {

    fun login(account: String, password: String) = flow {
        emit(api.login(account, password))
    }

    suspend fun saveUser(userEntity: UserEntity) {
        SharePreferencesUtils.instance.tempUserId = userEntity.id
        UserDataManager.instance.currentUserEntity = userEntity
        dataBase.userDao().insertUser(userEntity)
    }
}