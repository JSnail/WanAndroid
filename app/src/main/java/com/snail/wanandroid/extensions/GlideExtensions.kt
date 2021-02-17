package com.snail.wanandroid.extensions

import android.widget.ImageView
import com.snail.wanandroid.glide.GlideApp


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/

fun ImageView.loadImage(url:Any){
    GlideApp.with(this.context)
        .asBitmap()
        .load(url)
        .into(this)

}