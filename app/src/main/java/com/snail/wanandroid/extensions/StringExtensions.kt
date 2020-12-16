package com.snail.wanandroid.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.MainThread

@MainThread
fun String.Check(context: Context){
    if (this.isEmpty()){
        Toast.makeText(context,"内容不能为空", Toast.LENGTH_SHORT).show()
        return
    }
}