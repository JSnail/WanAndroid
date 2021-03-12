package com.snail.wanandroid.bindingAdapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.snail.wanandroid.extensions.loadCircleImage


/**
 * @Author  Snail
 * @Date 1/4/21
 * @Description
 **/
object HomeBindingAdapter {


    @BindingAdapter("imageUrl", "error")
    fun loadImage(
        view: ImageView,
        url: String,
        error: Drawable
    ) {

    }

}