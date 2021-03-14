package com.snail.wanandroid.api

import com.snail.wanandroid.base.BaseEntity
import com.snail.wanandroid.entity.*
import retrofit2.http.*

interface ApiService {

    /**
     *登录
     **/
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") account: String,
        @Field("password") password: String
    ): BaseEntity<UserEntity>

    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String, @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseEntity<UserEntity>

    /**
     *首页文章列表
     **/
    @GET("article/list/{page}/json")
    suspend fun getHomeArticleList(@Path("page") page: Int): BaseEntity<ArticleListEntity>

    /**
     *首页banner
     **/
    @GET("banner/json")
    suspend fun getHomeBanner(): BaseEntity<MutableList<BannerEntity>>

    /**
     *置顶文章
     **/
    @GET("article/top/json")
    suspend fun getArticleTop(): BaseEntity<MutableList<ArticleTopEntity>>


    /**
     *收藏站内文章
     **/
    @POST("lg/collect/{id}/json")
    suspend fun collectInnerArticle(@Path("id") id: Int): BaseEntity<Any>

    /**
     *收藏站外文章
     **/
    @POST("lg/collect/add/json")
    suspend fun collectOutArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): BaseEntity<Any>


    /**
     * 文章列表中取消收藏文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): BaseEntity<Any>

    /**
     * 收藏列表中取消收藏文章
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    suspend fun removeCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): BaseEntity<Any>


    /**
     * 获取个人积分，需要登录后访问
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getUserRankInfo(): BaseEntity<UserRankEntity>
}