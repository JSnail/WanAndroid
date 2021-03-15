package com.snail.wanandroid.ui.share

import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityRankDetailsBinding

class ShareActivity :BaseActivity<ActivityRankDetailsBinding>() {
    override fun getViewBinding(): ActivityRankDetailsBinding = ActivityRankDetailsBinding.inflate(layoutInflater)
    override fun loadData() {
    }
}