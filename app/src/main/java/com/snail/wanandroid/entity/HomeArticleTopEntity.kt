package com.snail.wanandroid.entity

class HomeArticleTopEntity (
    val data :MutableList<ArticleTopEntity>?
        ):BaseHomeAllEntity(){
    init {
        itemType = topArticle
    }
}