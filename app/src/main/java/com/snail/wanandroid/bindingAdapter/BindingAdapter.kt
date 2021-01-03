package com.snail.wanandroid.bindingAdapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * @Author  Snail
 * @Date 1/4/21
 * @Description
 **/
object BindingAdapter {


    @BindingAdapter("imageUrl", "error")
    fun loadImage(
        view: ImageView,
        url: String,
        error: Drawable
    ) {

    }

}