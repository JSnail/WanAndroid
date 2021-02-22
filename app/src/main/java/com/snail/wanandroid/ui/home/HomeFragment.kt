package com.snail.wanandroid.ui.home

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snail.banner.banner.layoutmanager.BannerLayoutManager
import com.snail.banner.banner.layoutmanager.RepeatLayoutManager
import com.snail.banner.banner.layoutmanager.StackLayoutManager
import com.snail.wanandroid.R
import com.snail.wanandroid.adapter.HomeAdapter
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
        vB.homeRecyclerView.apply {
            this.layoutManager = RepeatLayoutManager(RecyclerView.HORIZONTAL)
//            this.layoutManager = StackLayoutManager(context)
//            this.layoutManager = BannerLayoutManager()
                this.adapter = HomeAdapter(context, mutableListOf(
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30631.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
                "https://scpic.chinaz.net/files/pic/pic9/202101/apic30145.jpg"
            )
            )
        }
    }


    override fun startObserver() {
        super.startObserver()
        homeViewModel.allData.observe(this, {
            Log.d("TAG", "请求成功 == ")
        })
    }
}