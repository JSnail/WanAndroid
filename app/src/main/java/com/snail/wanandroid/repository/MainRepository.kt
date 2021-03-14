package com.snail.wanandroid.repository

import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MainRepository(private val apiService: ApiService) {

     fun getUserEntity(): MutableLiveData<UserEntity> {
        return UserDataManager.instance.currentUserEntity
    }

    suspend fun loadUserInfo(){
       withContext(Dispatchers.IO){
           UserDataManager.instance.getCurrentUserData()
       }
    }

    fun logout() {
        UserDataManager.instance.logout()
    }

    suspend fun getUserRankInfo() = flow {
        emit(apiService.getUserRankInfo())
    }

}