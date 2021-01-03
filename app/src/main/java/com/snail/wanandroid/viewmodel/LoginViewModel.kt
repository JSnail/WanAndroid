package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel constructor(private val loginRepository: LoginRepository) : BaseViewModel(),
    CoroutineScope by MainScope() {

    val loginLiveData = MutableLiveData<Boolean>()

    fun login(account: String, password: String) {
        launch {
            loginRepository.login(account, password)
                .onStart { dialogViewLiveData.value = true }

                .collectLatest {
                    loginLiveData.value = true
                }
        }
    }
}