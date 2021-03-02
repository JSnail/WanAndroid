package com.snail.wanandroid.ui.home

import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.extensions.loadCircleImage
import com.snail.wanandroid.extensions.loadImage
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()

    override fun loadData() {
        vB.holder = this


        homeViewModel.getHomeAllData()
        val data = mutableListOf(
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30631.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30523.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30160.jpg",
            "https://scpic.chinaz.net/files/pic/pic9/202101/apic30547.jpg"
        )
        vB.homeBanner.BannerBuilder().setData(data)
            .addLifecycleOwner(this)
            .isAutoPlaying(true)
            .setOnImageLoadListener { imageView, url ->
                imageView.loadImage(url)
            }
            .setOnItemClickListener { _, position ->
                Snackbar.make(vB.homeBanner, "position $position", Snackbar.LENGTH_SHORT).show()
            }
            .build()
        vB.textHome.loadCircleImage("https://scpic.chinaz.net/files/pic/pic9/202101/apic30631.jpg")
    }


    override fun startObserver() {
        super.startObserver()
        homeViewModel.allData.observe(this, {
            Log.d("TAG", "请求成功 == ")
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        vB.homeBanner.onHiddenChanged(hidden)
    }

}