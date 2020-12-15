package com.snail.wanandroid.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>
constructor(@LayoutRes private val layoutId: Int) : AppCompatActivity() {

    protected lateinit var vB: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vB = DataBindingUtil.setContentView(this, layoutId)
        loadData()

    }


    abstract fun loadData()


}