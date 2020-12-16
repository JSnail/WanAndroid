package com.snail.wanandroid.base

import android.app.Application
import com.snail.wanandroid.koin.repositoryModule
import com.snail.wanandroid.koin.viewModelModule
import org.koin.core.context.startKoin

class WanAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        mApplication = this

        initKoin()
    }

    private fun initKoin() {
        startKoin{
            modules(
                viewModelModule, repositoryModule
            )
        }
    }

    companion object {
        lateinit var mApplication: Application
    }
}