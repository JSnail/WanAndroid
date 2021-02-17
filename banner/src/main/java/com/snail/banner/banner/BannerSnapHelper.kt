package com.snail.banner.banner

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class BannerSnapHelper : SnapHelper() {

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
return  null
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return null
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
return 0
    }
}