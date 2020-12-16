package com.snail.wanandroid.network

import com.snail.wanandroid.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager {
    private var baseUrl = "https://www.wanandroid.com"

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(createClient())
        .build().create(ApiService::class.java)


    private fun createClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10000L, TimeUnit.SECONDS)
        .readTimeout(10000L, TimeUnit.SECONDS)
        .writeTimeout(10000L, TimeUnit.SECONDS)
        .build()
}