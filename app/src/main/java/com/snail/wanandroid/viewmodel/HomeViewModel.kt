package com.snail.wanandroid.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.entity.HomeBannerEntity
import com.snail.wanandroid.repository.HomeRepository
import com.snail.wanandroid.widget.MultiStateView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val homeRepository: HomeRepository) : BaseViewModel() {
    private var page = 0
    val allData = MutableLiveData<MutableList<BaseHomeAllEntity>>()

    val articleListData = MutableLiveData<MutableList<BaseHomeAllEntity>>()
    val multiStateView = ObservableField<MultiStateView.ViewState>().apply {
        this.set(MultiStateView.ViewState.LOADING)
    }

    fun getHomeAllData() {
        page = 0
        launch(handlerExpectation) {
            val banner = async { homeRepository.getBannerData() }
            val topArticle = async { homeRepository.getArticleTop() }
            val homeArticleList = async { homeRepository.getHomeArticleList(0) }

            val result = mutableListOf<BaseHomeAllEntity>()
            val bannerEntities = HomeBannerEntity(banner.await().recordset)
            result.add(bannerEntities)

          topArticle.await().recordset?.forEach {
              result.add(it)
          }
            homeArticleList.await().recordset?.datas?.forEach {
                result.add(it)
            }
            multiStateView.set(MultiStateView.ViewState.CONTENT)
            allData.value = result
        }

    }

    fun getArticleList() {
        page++
        launch(handlerExpectation) {
            val homeArticleList = async { homeRepository.getHomeArticleList(page) }
            val result = mutableListOf<BaseHomeAllEntity>()
            homeArticleList.await().recordset?.datas?.forEach {
                result.add(it)
            }
            articleListData.value = result
        }
    }

    override fun onErrorMessage(errorMessage: String) {
        multiStateView.set(MultiStateView.ViewState.ERROR)
    }
}