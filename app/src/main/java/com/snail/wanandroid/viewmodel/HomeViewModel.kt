package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    fun getHomeAllData() {
        page = 0
        viewModelScope.launch(handlerExpectation) {
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
            allData.value = result
        }

    }

    fun getArticleList() {
        page++
        viewModelScope.  launch(handlerExpectation) {
            val homeArticleList = async { homeRepository.getHomeArticleList(page) }
            val result = mutableListOf<BaseHomeAllEntity>()
            homeArticleList.await().recordset?.datas?.forEach {
                result.add(it)
            }
            articleListData.value = result
        }
    }

    fun collect(id: Int) {
        viewModelScope.launch(handlerExpectation) {
            homeRepository.collect(id)
        }
    }

    fun unCollect(id: Int) {
        viewModelScope.launch(handlerExpectation) {
            homeRepository.unCollect(id)
        }
    }

    override fun onErrorMessage(errorMessage: String) {
    }
}