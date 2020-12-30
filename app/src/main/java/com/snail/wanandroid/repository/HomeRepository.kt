package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.entity.HomeAllEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class HomeRepository constructor(private val apiService: ApiService) {

    suspend fun getHomeArticleList(page: Int) {

    }

    suspend fun getHomeAllData(): HomeAllEntity = coroutineScope {
        val banner = async { apiService.getHomeBanner() }
        val topArticle = async { apiService.getArticleTop() }
        val homeArticleList = async { apiService.getHomeArticleList(0) }

        return@coroutineScope HomeAllEntity(
            bannerEntity = banner.await().recordset,
            articleTopEntity = topArticle.await().recordset,
            articleListEntity = homeArticleList.await().recordset
        )
    }

}