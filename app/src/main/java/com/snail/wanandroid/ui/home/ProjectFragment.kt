package com.snail.wanandroid.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.databinding.FragmentProjectBinding

class ProjectFragment : BaseFragment<FragmentProjectBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProjectBinding =
        FragmentProjectBinding.inflate(inflater, container, false)
    override fun loadData() {
        vB.textDashboard.setOnClickListener {
            dialogViewLiveData.value = false
        }
    }


}