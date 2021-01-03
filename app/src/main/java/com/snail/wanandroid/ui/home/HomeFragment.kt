package com.snail.wanandroid.ui.home

import android.content.Intent
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel : HomeViewModel by viewModel()

    override fun loadData() {

        vB.textHome.onClick {
             val intent = Intent(requireActivity(),LoginActivity::class.java)
            startActivity(intent)
        }


    }


    override fun startObserver() {
        super.startObserver()

    }
}