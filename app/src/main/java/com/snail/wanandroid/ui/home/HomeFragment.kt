package com.snail.wanandroid.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.snail.wanandroid.adapter.HomeAdapter
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentHomeBinding
import com.snail.wanandroid.entity.ArticleListBean
import com.snail.wanandroid.entity.ArticleTopEntity
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.entity.WebDataEntity
import com.snail.wanandroid.listener.OnScrollToTopListener
import com.snail.wanandroid.ui.web.WebActivity
import com.snail.wanandroid.viewmodel.HomeViewModel
import com.snail.wanandroid.widget.home.ReboundingSwipeActionCallback
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.ParsePosition

class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnScrollToTopListener {

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

        override fun onItemClicked(cardView: View, position: Int,bean: BaseHomeAllEntity) {
         val title = when (bean) {
             is ArticleTopEntity ->  bean.title
             is ArticleListBean ->  bean.title
             else -> throw IllegalArgumentException("title error")
         }

            exitTransition = MaterialElevationScale(false).apply {
                duration =300L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration =300L
            }

            val intent = Intent(requireActivity(), WebActivity::class.java).apply {
                this.putExtra(WebActivity.URLS, homeViewModel.urlArray)
                this.putExtra(WebActivity.TITLE,title)
            }
            currentPosition = position
            val options = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                cardView,
                cardView.transitionName
            )
            goToActivity(intent,options)
        }

        override fun onStatusChanged(bean: BaseHomeAllEntity?, newValue: Boolean) {
            var id = 0
            if (bean is ArticleTopEntity) {
                id = bean.id
                if (bean.collect) {
                    homeViewModel.unCollect(bean.id)
                    bean.collect = false
                } else {
                    homeViewModel.collect(bean.id)
                    bean.collect = true
                }
            } else if (bean is ArticleListBean) {
                id = bean.id
                if (bean.collect) {
                    homeViewModel.unCollect(bean.id)
                    bean.collect = false
                } else {
                    homeViewModel.collect(bean.id)
                    bean.collect = true
                }
            }
            homeViewModel.updateWebUrlStatus(id)
        }

        override fun onCollected(bean: BaseHomeAllEntity) {
        }
    }

    override fun onResume() {
        super.onResume()
        if(currentPosition != -1 ){
            Log.d("TAG", "onResume: ")
            vB.contentView.smoothScrollToPosition(currentPosition)
        }
    }

    override fun onScrollToTop() {
        vB.contentView.smoothScrollToPosition(0)
    }

    companion object{
         var currentPosition = -1
    }

}