package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.entity.UserEntity
import com.snail.wanandroid.repository.RegisterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel constructor(private val registerRepository: RegisterRepository) :
    BaseViewModel() {

    val registerData = MutableLiveData<Boolean>()

    fun startRegister(account: String, password: String) {
        launch(handlerExpectation) {
            registerRepository.startRegister(account, password)
                .collectLatest {
                    if (null != it.recordset) {
                        registerData.value = true
                    } else {
                        errorMessage.value = it.errorMsg
                    }
                }
        }

    }


}