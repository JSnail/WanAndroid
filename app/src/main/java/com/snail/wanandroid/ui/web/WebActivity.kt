package com.snail.wanandroid.ui.web

import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityWebBinding

class WebActivity : BaseActivity<ActivityWebBinding>() {
    companion object {
        const val URL = "web_url"

    }

    override fun getViewBinding(): ActivityWebBinding {
        return ActivityWebBinding.inflate(layoutInflater)
    }

    override fun loadData() {
        val url = intent.getStringExtra(URL) ?: ""
    }
}