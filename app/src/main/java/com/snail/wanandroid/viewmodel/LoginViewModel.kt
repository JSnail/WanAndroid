package com.snail.wanandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel constructor(private val loginRepository: LoginRepository) : BaseViewModel(){

    val loginLiveData = MutableLiveData<Boolean>()

    fun login(account: String, password: String) {
        launch (handlerExpectation){
            loginRepository.login(account, password)
                .collectLatest {
                    loginLiveData.value = true
                }
        }
    }
}