package com.snail.wanandroid.ui.credits

import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityMainBinding
import com.snail.wanandroid.databinding.ActivityRankDetailsBinding

class RankActivity :BaseActivity<ActivityRankDetailsBinding>() {

    override fun getViewBinding(): ActivityRankDetailsBinding = ActivityRankDetailsBinding.inflate(layoutInflater)
    override fun loadData() {
    }
}