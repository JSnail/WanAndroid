package com.snail.wanandroid.network

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.network.interceptor.CookieInterceptor
import com.snail.wanandroid.network.interceptor.HeaderInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager {
    private var baseUrl = "https://www.wanandroid.com"
    private val moShi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moShi))
        .client(createClient())
        .build().create(ApiService::class.java)


    private fun createClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(CookieInterceptor() )
        .addInterceptor(HeaderInterceptor())
        .connectTimeout(10000L, TimeUnit.SECONDS)
        .readTimeout(10000L, TimeUnit.SECONDS)
        .writeTimeout(10000L, TimeUnit.SECONDS)
        .build()
}