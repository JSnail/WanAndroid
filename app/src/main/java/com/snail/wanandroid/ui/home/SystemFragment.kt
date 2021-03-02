package com.snail.wanandroid.ui.home

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentSystemBinding

class SystemFragment : BaseFragment<FragmentSystemBinding>(R.layout.fragment_system) {
    override fun loadData() {
        vB.textDashboard.setOnClickListener {
            dialogViewLiveData.value = false
        }
    }


}