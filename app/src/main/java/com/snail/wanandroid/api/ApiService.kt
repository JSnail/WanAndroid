package com.snail.wanandroid.api

import com.snail.wanandroid.base.BaseEntity
import com.snail.wanandroid.entity.UserEntity
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("/user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String, @Field("password") password: String,
        @Field("repassword") repassword: String
    ): BaseEntity<UserEntity>

}