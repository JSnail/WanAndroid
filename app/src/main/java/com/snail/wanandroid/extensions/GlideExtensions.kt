package com.snail.wanandroid.extensions

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

fun ImageView.loadRoundImage(url:Any){
    GlideApp.with(context)
        .load(url)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}