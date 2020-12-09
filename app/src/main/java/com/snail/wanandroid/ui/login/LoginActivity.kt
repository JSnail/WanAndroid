package com.snail.wanandroid.ui.login

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_login

    override fun loadData() {
        ActivityLoginBinding.inflate(layoutInflater)
    }
}