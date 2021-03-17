package com.snail.wanandroid.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.snail.wanandroid.R
import com.snail.wanandroid.adapter.HomeAdapter
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.entity.ArticleListBean
import com.snail.wanandroid.entity.ArticleTopEntity
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.ui.web.WebActivity
import com.snail.wanandroid.viewmodel.HomeViewModel
import com.snail.wanandroid.widget.home.ReboundingSwipeActionCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalArgumentException

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val homeAdapter by lazy {
        HomeAdapter(requireActivity(), adapterListener)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun loadData() {

        initView()
        vB.homeRefreshLayout.autoRefresh()

        vB.homeRefreshLayout.addOnRefreshListener {
            homeViewModel.getHomeAllData()
        }.addOnLoadMoreListener {
            homeViewModel.getArticleList()
        }
    }

    private fun initView() {
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
        homeViewModel.articleListData.observe(this, {
            homeAdapter.addData(it)
            vB.homeRefreshLayout.lordMoreComplete()
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        homeAdapter.onHiddenChanged(hidden)
    }

    private val adapterListener = object : HomeAdapter.HomeAdapterListener {

        override fun onItemClicked(cardView: View, bean: BaseHomeAllEntity) {
         val url = when (bean) {
             is ArticleTopEntity ->  bean.link
             is ArticleListBean ->  bean.link
             else -> throw IllegalArgumentException("url error")
         }

            exitTransition = MaterialElevationScale(false).apply {
                duration =300L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration =300L
            }

            val extras = FragmentNavigatorExtras(cardView to getString(R.string.transitionName_home))

            val intent = Intent(requireActivity(), WebActivity::class.java).apply {
                this.putExtra(WebActivity.URL,url)
            }
            goToActivity(intent)
        }

        override fun onStatusChanged(bean: BaseHomeAllEntity?, newValue: Boolean) {
            if (bean is ArticleTopEntity) {
                if (bean.collect) {
                    homeViewModel.unCollect(bean.id)
                    bean.collect = false
                } else {
                    homeViewModel.collect(bean.id)
                    bean.collect = true
                }
            } else if (bean is ArticleListBean) {
                if (bean.collect) {
                    homeViewModel.unCollect(bean.id)
                    bean.collect = false
                } else {
                    homeViewModel.collect(bean.id)
                    bean.collect = true
                }
            }
        }

        override fun onCollected(bean: BaseHomeAllEntity) {
        }
    }


}