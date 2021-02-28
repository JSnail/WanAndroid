package com.snail.banner.banner.snap

import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnFlingListener
import com.snail.banner.banner.layoutmanager.BannerLayoutManager
import com.snail.banner.banner.listener.OnBannerPageChangeListener
import kotlin.math.abs

class BannerSnapHelper : OnFlingListener() {
    private var mRecyclerView: RecyclerView? = null
    private lateinit var mGravityScroller: Scroller
    private var snapToCenter = false

    private val mScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            var mScrolled = false
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as BannerLayoutManager
                val onPageChangeListener = layoutManager.onBannerPageChangeListener
                onPageChangeListener?.onPageScrollStateChanged(newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mScrolled) {
                    mScrolled = false
                    if (!snapToCenter) {
                        snapToCenter = true
                        snapToCenterView(layoutManager, onPageChangeListener)
                    } else {
                        snapToCenter = false
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dx != 0 || dy != 0) {
                    mScrolled = true
                }
            }
        }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        mRecyclerView?.let {
            val layoutManager = it.layoutManager as BannerLayoutManager
            it.adapter ?: return false
            if ((layoutManager.mOffset == layoutManager.getMaxOffset()
                        || layoutManager.mOffset == layoutManager.getMinOffset())
            ) {
                return false
            }
            val minFlingVelocity = it.minFlingVelocity
            mGravityScroller.fling(
                0,
                0,
                velocityX,
                velocityY,
                Int.MIN_VALUE,
                Int.MAX_VALUE,
                Int.MIN_VALUE,
                Int.MAX_VALUE
            )
            if (abs(velocityX) > minFlingVelocity) {
                val currentPosition = layoutManager.getCurrentPosition()
                val offsetPosition = mGravityScroller.finalX / layoutManager.mItemGap
                it.smoothScrollToPosition((currentPosition + offsetPosition).toInt())
                return true
            }
        }

        return true
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        if (null != mRecyclerView && mRecyclerView == recyclerView) {
            return
        }
        destroyCallbacks()
        mRecyclerView = recyclerView
        val layoutManager = mRecyclerView?.layoutManager as? BannerLayoutManager ?: return
        setupCallbacks()
        mGravityScroller = Scroller(
            mRecyclerView?.context,
            DecelerateInterpolator()
        )
        snapToCenterView(
            layoutManager,
            layoutManager.onBannerPageChangeListener
        )
    }

    fun snapToCenterView(
        layoutManager: BannerLayoutManager,
        listener: OnBannerPageChangeListener?
    ) {
        val delta = layoutManager.getOffsetToCenter()
        if (delta != 0) {
            mRecyclerView?.smoothScrollBy(delta, 0)
        } else {
            snapToCenter = false
        }
        listener?.onPageSelected(layoutManager.getCurrentPosition())
    }


    private fun setupCallbacks() {
        mRecyclerView?.addOnScrollListener(mScrollListener)
        mRecyclerView?.onFlingListener = this
    }


    private fun destroyCallbacks() {
        mRecyclerView?.removeOnScrollListener(mScrollListener)
        mRecyclerView?.onFlingListener = null
    }
}