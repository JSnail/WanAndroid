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

    suspend fun collect(id:Int) = apiService.collectInnerArticle(id)

    suspend fun unCollect(id:Int) = apiService.cancelCollectArticle(id)




}