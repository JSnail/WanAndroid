package com.snail.wanandroid.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.snail.wanandroid.BR
import com.snail.wanandroid.base.ObservableViewModel
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.entity.UserRankEntity
import com.snail.wanandroid.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel constructor(private val repository: MainRepository) : ObservableViewModel() {
    var userEntity: UserEntity? = null
    var isNeedLogin = MutableLiveData<Boolean>()
    var userRankEntity: UserRankEntity? = null

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userEntity = repository.getUserEntity()
            }
            getUserRankInfo()
        }
    }

    var isLogin: Boolean = false
        @Bindable
        get() = UserDataManager.instance.isLogged
        @Bindable
        set(value) {
            getUserRankInfo()
            notifyPropertyChanged(BR.login)
            field = value
        }


    val nickName = ObservableField<String>(userEntity?.nickname)
    val rank = ObservableField((userRankEntity?.rank ?: 0).toString())

    fun refreshLoginStatus() {
        notifyPropertyChanged(BR.login)
    }

    fun loginOrLogout() {
        if (!isLogin) {
            isNeedLogin.value = true
        } else {
            repository.logout()
        }
    }

    private fun getUserRankInfo() {
        viewModelScope.launch {
            repository.getUserRankInfo()
                .catch { flo ->
                    Log.i("TAG", "  ${flo.message}")
                }
                .collectLatest {
                    userRankEntity = it.recordset
                    Log.i("TAG", "getUserRankInfo ==   ${userRankEntity?.username}")
                    rank.set(userRankEntity?.rank?.toString() ?: "--")
                    nickName.set(userRankEntity?.username)
                }
        }
    }


}