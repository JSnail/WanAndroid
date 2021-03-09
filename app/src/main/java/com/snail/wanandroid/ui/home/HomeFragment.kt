package com.snail.wanandroid.ui.home

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.snail.wanandroid.R
import com.snail.wanandroid.adapter.HomeAdapter
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()
    private val homeAdapter by lazy {
        HomeAdapter(requireActivity())
    }

    override fun loadData() {
        vB.viewModel = homeViewModel

        initView()
        vB.homeRefreshLayout.autoRefresh()

        vB.homeRefreshLayout.addOnRefreshListener {
            homeViewModel.getHomeAllData()
        }.addOnLoadMoreListener {
            homeViewModel.getArticleList()
        }
    }

    private fun initView(){
        vB.contentView.apply {
            this.adapter = homeAdapter
            this.layoutManager = LinearLayoutManager(requireActivity())
        }
    }


    override fun startObserver() {
        super.startObserver()
        homeViewModel.allData.observe(this, {
            homeAdapter.setData(it)
            vB.homeRefreshLayout.refreshComplete()
            vB.homeRefreshLayout.lordMoreComplete()
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        vB.homeBanner.onHiddenChanged(hidden)
    }

}