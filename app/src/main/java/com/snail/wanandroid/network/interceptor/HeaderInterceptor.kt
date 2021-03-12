package com.snail.wanandroid.network.interceptor

import android.util.Log
import com.snail.wanandroid.network.CookieManager
import com.snail.wanandroid.network.RequestContent
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.java.KoinJavaComponent.inject

class HeaderInterceptor : Interceptor {

    private val cookieManager:CookieManager by inject(CookieManager::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type", "application/json; charset=utf-8")
        if (CookieManager().getCookie().isNotEmpty()){
            builder.addHeader(RequestContent.COOKIE_NAME, cookieManager.getCookie())
        }

        return chain.proceed(builder.build())
    }
}