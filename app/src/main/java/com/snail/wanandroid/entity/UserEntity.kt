package com.snail.wanandroid.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey     val id: Int,
    val admin: Boolean,
//    val chapterTops: List<Any>,
    val coinCount: Int,
//    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    var nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)
