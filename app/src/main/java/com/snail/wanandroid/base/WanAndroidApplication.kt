package com.snail.wanandroid.base

import android.app.Application
import com.snail.wanandroid.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WanAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this

        initKoin()
    }

    private fun initKoin() {
        startKoin{
            androidContext(this@WanAndroidApplication)
            modules(
                viewModelModule, repositoryModule,dialogModule, retrofitModule, dataBaseModule
            )
        }
    }

    companion object {
        lateinit var mApplication: Application
    }
}