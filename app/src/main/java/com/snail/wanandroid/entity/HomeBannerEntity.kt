package com.snail.wanandroid.entity

class HomeBannerEntity(
    val bannerDatas:MutableList<BannerEntity>?
):  BaseHomeAllEntity() {
    init {
        itemType = BaseHomeAllEntity.banner
    }
}