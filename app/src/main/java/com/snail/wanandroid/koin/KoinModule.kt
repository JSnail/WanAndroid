package com.snail.wanandroid.koin

import com.snail.wanandroid.network.RetrofitManager
import com.snail.wanandroid.repository.LoginRepository
import com.snail.wanandroid.repository.RegisterRepository
import com.snail.wanandroid.viewmodel.LoginViewModel
import com.snail.wanandroid.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}

val repositoryModule = module {
    single { LoginRepository(get()) }
    single { RegisterRepository(get()) }
}

val retrofitModule = module {
    single { RetrofitManager().apiService }

}