package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.entity.UserRankEntity
import com.snail.wanandroid.repository.MainRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: MainRepository) : BaseViewModel() {
    var userEntity: MutableLiveData<UserEntity> = repository.getUserEntity()
    var userRankEntity = MutableLiveData<UserRankEntity>()

    init {
        viewModelScope.launch {
            repository.loadUserInfo()
        }
    }


    var isLogin = false
        get() = UserDataManager.instance.isLogged
        set(value) {
            UserDataManager.instance.isLogged = value
            field = value
        }


    fun logout() {
        repository.logout()
        isLogin = false
        userRankEntity.value = null
    }

    fun getUserRankInfo() {
        isLogin = true
        viewModelScope.launch {
            repository.getUserRankInfo()
                .collectLatest {
                    userRankEntity.value = it.recordset
                }
        }
    }

}