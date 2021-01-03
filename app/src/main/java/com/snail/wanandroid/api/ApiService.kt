package com.snail.wanandroid.api

import com.snail.wanandroid.base.BaseEntity
import com.snail.wanandroid.entity.ArticleListEntity
import com.snail.wanandroid.entity.ArticleTopEntity
import com.snail.wanandroid.entity.BannerEntity
import com.snail.wanandroid.entity.UserEntity
import retrofit2.http.*

interface ApiService {

    /**
      *登录
      **/
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username") account:String,@Field("password") password: String):BaseEntity<UserEntity>

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
    suspend fun getHomeArticleList(@Path("page") page: Int):BaseEntity<ArticleListEntity>

    /**
      *首页banner
      **/
    @GET("banner/json")
    suspend fun getHomeBanner():BaseEntity<BannerEntity>

    /**
      *置顶文章
      **/
    @GET("article/top/json")
    suspend fun getArticleTop() :BaseEntity<ArticleTopEntity>


}