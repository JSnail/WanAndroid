package com.snail.wanandroid.entity

/**
 *首页文章列表
 **/
data class ArticleListEntity(
    val curPage: Int,
    val datas: MutableList<ArticleListBean>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)