package com.snail.wanandroid.ui.home

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun loadData() {
        vB.textHome.setOnClickListener {
//            startActivity(Intent(activity,LoginActivity::class.java))
//            dialogViewLiveData.value = true
        }


    }


    override fun startObserver() {
        super.startObserver()

    }
}