package com.snail.banner.banner.listener

import android.widget.ImageView


/**
 * @Author  Snail
 * @Date 2/27/21
 * @Description
 **/
interface OnImageLoadListener {
    fun onLoadImage(view:ImageView,url:String)
}