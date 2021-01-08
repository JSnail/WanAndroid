package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService
import com.snail.wanandroid.entity.BaseHomeAllEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class HomeRepository constructor(private val apiService: ApiService) {

    suspend fun getHomeArticleList(page: Int)   = apiService.getHomeArticleList(page)

    suspend fun getBannerData() = apiService.getHomeBanner()

    suspend fun getArticleTop() = apiService.getArticleTop()


//    suspend fun getHomeAllData(): ArrayList<BaseHomeAllEntity> = coroutineScope {
//        val banner = async { apiService.getHomeBanner() }
//        val topArticle = async { apiService.getArticleTop() }
//        val homeArticleList = async { apiService.getHomeArticleList(0) }
//        val result = ArrayList<BaseHomeAllEntity>()
//
//
//        return@coroutineScope BaseHomeAllEntity(
//            bannerEntity = banner.await().recordset,
//            articleTopEntity = topArticle.await().recordset,
//            articleListEntity = homeArticleList.await().recordset
//        )
//    }



}