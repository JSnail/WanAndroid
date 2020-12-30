package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepository constructor(private val apiService: ApiService) {

    suspend fun getHomeArticleList(page: Int) {

    }

    suspend fun getHomeAllData() = coroutineScope {
        val banner = async { apiService.getHomeBanner() }
    }

}