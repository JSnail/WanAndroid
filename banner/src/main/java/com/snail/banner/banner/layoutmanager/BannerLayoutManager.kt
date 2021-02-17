package com.snail.banner.banner.layoutmanager

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class BannerLayoutManager : RecyclerView.LayoutManager() {

    private var centerToLeftDistance = -1
    private var rightToCenterDistance = -1
    private var horizontalOffset = -1
    private var viewMargin = 30
    private var childWidth = -1
    private var viewPosition = 0


    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        if (state.itemCount ==  0  || state.isPreLayout ){
            removeAndRecycleAllViews(recycler)
            return
        }
        centerToLeftDistance = -1
        detachAndScrapAttachedViews(recycler)
        onLayout(recycler,state)
        recyclerView(recycler)
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {


        return super.scrollHorizontallyBy(dx, recycler, state)
    }

    override fun onScrollStateChanged(state: Int) {
        when (state) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
            }
            RecyclerView.SCROLL_STATE_IDLE -> {
            }
        }
    }

    private fun onLayout(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        var tempView: View
        var tempPosition = -1

        if (rightToCenterDistance == -1){
            tempPosition = 0
            tempView = recycler.getViewForPosition(0)
        }

    }

    /**
      *回收
      **/
    private fun recyclerView(recycler: RecyclerView.Recycler){

    }
}