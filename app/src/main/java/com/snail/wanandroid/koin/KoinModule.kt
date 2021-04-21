package com.snail.wanandroid.koin

import com.snail.wanandroid.db.AppDataBase
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.network.CookieManager
import com.snail.wanandroid.network.RetrofitManager
import com.snail.wanandroid.repository.*
import com.snail.wanandroid.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { WebViewModel(get()) }
}

val repositoryModule = module {
    single { LoginRepository(get(), get()) }
    single { RegisterRepository(get()) }
    single { HomeRepository(get()) }
    single { MainRepository(get()) }
    single { WebRepository(get()) }
}

val retrofitModule = module {
    single { RetrofitManager().apiService }
    single { CookieManager() }
}

val dialogModule = module {
    single { LoadingViewLiveData() }
    single { LoadingDialog() }
}

val dataBaseModule = module(createdAtStart = true) {
    single { AppDataBase.getInstance(get()) }
}