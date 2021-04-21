package com.snail.wanandroid.repository

import com.snail.wanandroid.api.ApiService

class WebRepository  constructor(private val apiService: ApiService)  {

    suspend fun collect(id:Int) = apiService.collectInnerArticle(id)

    suspend fun unCollect(id:Int) = apiService.cancelCollectArticle(id)
}