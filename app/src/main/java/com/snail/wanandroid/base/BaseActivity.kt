package com.snail.wanandroid.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    protected lateinit var vB: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vB = getViewBinding()
        setContentView(vB.root)
        loadData()
        startObserver()
    }

    abstract fun getViewBinding(): T

    open fun goToActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }

    open fun goToActivityForResult(cls: Class<*>, requestId: Int) {
        startActivityForResult(Intent(this, cls), requestId)
    }


    abstract fun loadData()

    open fun startObserver() {

    }
}