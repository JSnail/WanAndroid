package com.snail.wanandroid.network.interceptor

import android.util.Log
import com.snail.wanandroid.network.CookieManager
import com.snail.wanandroid.network.RequestContent
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Content-type", "application/json; charset=utf-8")
//        val url = request.url.toString()
//        if (url.contains())
        builder.header(RequestContent.SET_COOKIE_KEY,CookieManager().getCookie())
        Log.i("TAG","cookie == ${CookieManager().getCookie()}" )

            return chain.proceed(request)
    }
}