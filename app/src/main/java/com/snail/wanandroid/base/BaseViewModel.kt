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
import org.koin.experimental.property.inject

@OptIn(KoinApiExtension::class)
open class BaseViewModel : ViewModel(), KoinComponent, CoroutineScope by MainScope()  {
     val dialogViewLiveData by inject<LoadingViewLiveData>()
     val errorMessage = MediatorLiveData<String>()
     protected val  handlerExpectation = CoroutineExceptionHandler{ _, throwable ->
          Log.d("TAG","throwable  --->  ${throwable.message}")
     }

}