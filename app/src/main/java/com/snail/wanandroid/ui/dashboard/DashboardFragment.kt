package com.snail.wanandroid.ui.dashboard

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentDashboardBinding

class DashboardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {
    override fun loadData() {
        vB.textDashboard.setOnClickListener {
            dialogViewLiveData.value = false
        }
    }


}