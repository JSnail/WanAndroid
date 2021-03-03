package com.snail.wanandroid.ui.home

import androidx.databinding.BindingAdapter
import com.snail.wanandroid.widget.MultiStateView

object TestAapter {
    @BindingAdapter("setViewState")
    @JvmStatic  fun setViewState(view:MultiStateView,state: MultiStateView.ViewState){
        view.viewState = state
    }
}