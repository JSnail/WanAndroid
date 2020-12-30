package com.snail.wanandroid.extensions

import android.os.SystemClock
import android.view.View

fun View.onClick(onClick :(View) ->Unit){
     var lastTime =0L
    this.setOnClickListener {
        val currentTime  = SystemClock.currentThreadTimeMillis()
        if (currentTime - lastTime >500){
            onClick.invoke(it)
            lastTime = currentTime
        }
    }
}


