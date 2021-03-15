package com.snail.wanandroid.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.databinding.FragmentNavigationBinding

class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentNavigationBinding =
        FragmentNavigationBinding.inflate(inflater, container, false)
    override fun loadData() {

    }
}