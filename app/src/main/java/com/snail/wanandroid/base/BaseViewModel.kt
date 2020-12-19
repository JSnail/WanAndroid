package com.snail.wanandroid.base

import androidx.lifecycle.ViewModel
import com.snail.wanandroid.viewmodel.LoadingViewLiveData
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.experimental.property.inject

@OptIn(KoinApiExtension::class)
open class BaseViewModel : ViewModel(), KoinComponent {
     val dialogViewLiveData by inject<LoadingViewLiveData>()

}