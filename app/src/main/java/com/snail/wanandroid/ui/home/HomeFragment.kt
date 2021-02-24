package com.snail.wanandroid.ui.home

import android.content.Intent
import android.util.Log
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun loadData() {
        vB.holder = this

        vB.textHome.onClick {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.getHomeAllData()
        val data = mutableListOf(
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30631.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg"
        )
        vB.homeBanner.startLoop(data, this)
    }


    override fun startObserver() {
        super.startObserver()
        homeViewModel.allData.observe(this, {
            Log.d("TAG", "请求成功 == ")
        })
    }
}