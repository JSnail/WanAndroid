package com.snail.wanandroid.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebDataEntity(
    val id: Int,
    val url:String,
    var isCollect:Boolean
): Parcelable