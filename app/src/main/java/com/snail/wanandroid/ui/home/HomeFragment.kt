package com.snail.wanandroid.ui.home

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.snail.wanandroid.widget.home.ReboundingSwipeActionCallback
import com.snail.wanandroid.R
import com.snail.wanandroid.adapter.HomeAdapter
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.entity.ArticleListBean
import com.snail.wanandroid.entity.ArticleTopEntity
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.entity.TestEntity
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()
    private val homeAdapter by lazy {
        HomeAdapter(requireActivity(),adapterListener)
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
            val itemTouchHelper = ItemTouchHelper(ReboundingSwipeActionCallback())
            itemTouchHelper.attachToRecyclerView(this)
            this.layoutManager = LinearLayoutManager(requireActivity())
        }
    }


    override fun startObserver() {
        super.startObserver()
        homeViewModel.allData.observe(this, {
            homeAdapter.setData(it)
            vB.homeRefreshLayout.refreshComplete()
        })
        homeViewModel.articleListData.observe(this,{
            homeAdapter.addData(it)
            vB.homeRefreshLayout.lordMoreComplete()
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
       homeAdapter.onHiddenChanged(hidden)
    }

    private val adapterListener = object  :HomeAdapter.HomeAdapterListener{

        override fun onItemClicked(cardView: View, bean: BaseHomeAllEntity) {
            if (bean is ArticleTopEntity){
            }else{
            }

        }

        override fun onStatusChanged(bean: BaseHomeAllEntity?, newValue: Boolean) {
            if (bean is ArticleTopEntity){
                if ( bean.collect){
                    homeViewModel.unCollect(bean.id)
                    bean.collect =false
                }else{
                    homeViewModel.collect(bean.id)
                    bean.collect =true
                }
            }else if (bean is ArticleListBean){
                if ( bean.collect){
                    homeViewModel.unCollect(bean.id)
                    bean.collect =false
                }else{
                    homeViewModel.collect(bean.id)
                    bean.collect =true
                }
            }
        }
        override fun onCollected(bean: BaseHomeAllEntity) {
        }
    }
}