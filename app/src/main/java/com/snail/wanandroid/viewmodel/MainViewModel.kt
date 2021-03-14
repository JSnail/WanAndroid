package com.snail.wanandroid.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.snail.wanandroid.BR
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.entity.UserRankEntity
import com.snail.wanandroid.repository.MainRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel constructor(private val repository: MainRepository) : BaseViewModel() {
    var userEntity: MutableLiveData<UserEntity> = repository.getUserEntity()
    var isNeedLogin = MutableLiveData<Boolean>()
    var userRankEntity = MutableLiveData<UserRankEntity>()

    init {
        viewModelScope.launch {
            repository.loadUserInfo()
        }
    }


    var isLogin = ObservableBoolean(false)
        get() = ObservableBoolean(UserDataManager.instance.isLogged)
        set(value) {
            UserDataManager.instance.isLogged = value.get()
            field = value
        }


    fun loginOrLogout() {
        if (!isLogin.get()) {
            isNeedLogin.value = true
        } else {
            repository.logout()
        }
    }

    fun getUserRankInfo() {
        isLogin.set(true)
        viewModelScope.launch {
            repository.getUserRankInfo()
                .collectLatest {
                    userRankEntity.value = it.recordset
                    Log.i("TAG", "  ${it.errorMsg}   ${Thread.currentThread().name}")
                }
        }
    }


}