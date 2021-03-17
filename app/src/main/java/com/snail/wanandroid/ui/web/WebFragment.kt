package com.snail.wanandroid.ui.web

import android.view.LayoutInflater
import android.view.ViewGroup
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentWebBinding

class WebFragment:BaseFragment<FragmentWebBinding>() {

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWebBinding  = FragmentWebBinding.inflate(inflater,container,false)
    override fun loadData() {

    }
}