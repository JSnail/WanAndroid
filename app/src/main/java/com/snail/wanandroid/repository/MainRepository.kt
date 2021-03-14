package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import kotlinx.coroutines.flow.flow

class MainRepository(private val apiService: ApiService) {

    suspend fun getUserEntity(): UserEntity? {
        return UserDataManager.instance.getCurrentUserData()
    }

    fun logout() {
        UserDataManager.instance.logout()
    }

    suspend fun getUserRankInfo() = flow {
        emit(apiService.getUserRankInfo())
    }

}