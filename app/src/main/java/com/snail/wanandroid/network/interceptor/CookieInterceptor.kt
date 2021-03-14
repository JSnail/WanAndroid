package com.snail.wanandroid.network.interceptor

import androidx.core.net.toUri
import com.snail.wanandroid.network.CookieManager
import com.snail.wanandroid.network.RequestContent
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.java.KoinJavaComponent.inject

class CookieInterceptor : Interceptor {
    private val cookieManager:CookieManager by inject(CookieManager::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val builder = request.newBuilder()
        builder.addHeader("Content-type", "application/json; charset=utf-8")
        val url = request.url.toString()
        if ((url.contains(RequestContent.LOGIN )
                    || url.contains(RequestContent.REGISTER))
            && response.headers(RequestContent.SET_COOKIE_KEY).isNotEmpty()){
            val cookies = response.headers(RequestContent.SET_COOKIE_KEY)
            cookieManager.saveCookie(cookies)
        }

        return  response
    }
}