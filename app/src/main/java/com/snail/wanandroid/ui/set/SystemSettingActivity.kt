package com.snail.wanandroid.ui.set

import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityRankDetailsBinding

class SystemSettingActivity :BaseActivity<ActivityRankDetailsBinding>() {
    override fun getViewBinding(): ActivityRankDetailsBinding = ActivityRankDetailsBinding.inflate(layoutInflater)
    override fun loadData() {
    }
}