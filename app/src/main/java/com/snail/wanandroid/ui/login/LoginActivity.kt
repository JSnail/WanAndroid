package com.snail.wanandroid.ui.login

import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun loadData() {
//        ActivityLoginBinding.inflate(layoutInflater)

    }
}