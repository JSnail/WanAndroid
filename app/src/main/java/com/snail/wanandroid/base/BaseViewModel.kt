package com.snail.wanandroid.base

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.snail.wanandroid.viewmodel.LoadingViewLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
open class BaseViewModel : ViewModel(), KoinComponent  {
    val errorMessage = MediatorLiveData<String>()
    protected val handlerExpectation = CoroutineExceptionHandler { _, throwable ->
        Log.e("TAG", "throwable  --->  ${throwable.message}")
        val message = throwable.message.toString()
        errorMessage.value = message
        onErrorMessage(message)
    }

  open   fun onErrorMessage(errorMessage: String){}
}