package com.snail.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snail.wanandroid.base.BaseViewModel
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel constructor(private val homeRepository: HomeRepository) : BaseViewModel() {

    val allData = MutableLiveData<MutableList<BaseHomeAllEntity>>()

    fun getHomeAllData() {
        launch(handlerExpectation) {
            val banner = async { homeRepository.getBannerData() }
            val topArticle = async { homeRepository.getArticleTop() }
            val homeArticleList = async { homeRepository.getHomeArticleList(0) }

            val result = mutableListOf<BaseHomeAllEntity>()
            banner.await().recordset?.forEach {
                result.add(it)
            }
            topArticle.await().recordset?.forEach {
                result.add(it)
            }
            homeArticleList.await().recordset?.datas?.forEach {
                result.add(it)
            }

            allData.value = result
        }

    }
}