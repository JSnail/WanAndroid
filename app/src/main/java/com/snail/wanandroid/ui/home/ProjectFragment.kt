package com.snail.wanandroid.ui.home

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentProjectBinding

class ProjectFragment : BaseFragment<FragmentProjectBinding>(R.layout.fragment_project) {
    override fun loadData() {
        vB.textDashboard.setOnClickListener {
            dialogViewLiveData.value = false
        }
    }


}