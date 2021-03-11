package com.snail.wanandroid.koin

import com.snail.wanandroid.db.AppDataBase
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.network.CookieManager
import com.snail.wanandroid.network.RetrofitManager
import com.snail.wanandroid.repository.HomeRepository
import com.snail.wanandroid.repository.LoginRepository
import com.snail.wanandroid.repository.RegisterRepository
import com.snail.wanandroid.viewmodel.HomeViewModel
import com.snail.wanandroid.viewmodel.LoadingViewLiveData
import com.snail.wanandroid.viewmodel.LoginViewModel
import com.snail.wanandroid.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}

val repositoryModule = module {
    single { LoginRepository(get(),get()) }
    single { RegisterRepository(get()) }
    single { HomeRepository(get()) }
}

val retrofitModule = module {
    single { RetrofitManager().apiService }
    single { CookieManager() }
}

val dialogModule = module {
    single { LoadingViewLiveData() }
    single { LoadingDialog() }
}

val dataBaseModule = module {
    single { AppDataBase.getInstance(get()) }
}