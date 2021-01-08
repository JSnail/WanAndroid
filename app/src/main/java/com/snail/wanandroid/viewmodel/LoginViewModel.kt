package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    val loginLiveData = MutableLiveData<Boolean>()

    fun login(account: String, password: String) {
        launch(handlerExpectation) {
            loginRepository.login(account, password)
                .collectLatest {
                    if (it.recordset == null) {
                        loginLiveData.value = false
                        errorMessage.value = it.errorMsg
                    } else {
                        loginLiveData.value = true
                        saveUser(it.recordset!!)
                    }

                }
        }
    }

    private suspend fun saveUser(userEntity: UserEntity) {
        withContext(Dispatchers.IO) {
            loginRepository.saveUser(userEntity)
        }
    }
}