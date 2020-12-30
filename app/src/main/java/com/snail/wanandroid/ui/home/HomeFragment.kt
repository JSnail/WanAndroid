package com.snail.wanandroid.ui.home

import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel : HomeViewModel by viewModel()

    override fun loadData() {

        vB.textHome.onClick {

        }


    }


    override fun startObserver() {
        super.startObserver()

    }
}