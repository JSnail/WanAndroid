package com.snail.banner.banner.layoutmanager

import android.graphics.PointF
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.snail.banner.banner.listener.OnBannerPageChangeListener
import kotlin.math.abs
import kotlin.math.roundToInt

class BannerLayoutManager : RecyclerView.LayoutManager(),
    RecyclerView.SmoothScroller.ScrollVectorProvider {

    var mOffset = 0F

    /**
     *完整显示的item宽度
     **/
    private var mDecoratedMeasurementWidth = 0

    /**
     *完整显示item高度
     **/
    private var mDecoratedMeasurementHeight = 0

    /**
     *除开完整显示的item两端 剩余的宽度
     **/
    private var mRemainWidth = 0

    /**
     * 因为需要进行缩放，所以item实际显示的高度会小于实际高度，需要在layout的时候进行调整
     **/
    private var mSpaceInVertical = 0

    /**
     *item之间的间距
     **/
    var mItemGap = 0F

    /**
     *缩放比例
     **/
    private val mScale = 1.2F


    private val mOrientationHelper by lazy {
        OrientationHelper.createOrientationHelper(
            this,
            RecyclerView.HORIZONTAL
        )
    }

    var onBannerPageChangeListener: OnBannerPageChangeListener? = null


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }


    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        if (childCount == 0) return null
        getChildAt(0)?.let {
            val firstChildPosition = getPosition(it)
            val direction = if (firstChildPosition > targetPosition) -1 else 1
            return PointF(direction.toFloat(), 0F)
        }
        return null
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val targetPosition = getOffsetToPosition(position)
        recyclerView.smoothScrollBy(targetPosition, 0)
        if (position == itemCount) {
            mOffset = -mItemGap
        }
    }

    private fun getOffsetToPosition(position: Int): Int {
        return (getCurrentPosition() + (position - getCurrentPosition()) * mItemGap - mOffset).toInt()
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            mOffset = 0F
            return
        }

        val scrapView = recycler.getViewForPosition(0)
        measureChildWithMargins(scrapView, 0, 0)
        mDecoratedMeasurementWidth = mOrientationHelper.getDecoratedMeasurement(scrapView)
        mDecoratedMeasurementHeight = mOrientationHelper.getDecoratedMeasurementInOther(scrapView)
        mRemainWidth = (mOrientationHelper.totalSpace - mDecoratedMeasurementWidth) / 2

        mSpaceInVertical = (getTotalSpace() - mDecoratedMeasurementHeight) / 2
        initItemGap()


        layout(recycler)
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State?
    ): Int {
        if (itemCount <= 1 || dx == 0) {
            return 0
        }
        return scroll(recycler, dx)
    }

    private fun scroll(recycler: RecyclerView.Recycler, dx: Int): Int {
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            mOffset = 0F
            return 0
        }
        mOffset += dx
        layout(recycler)
        recycleViews(recycler, dx)
        return dx
    }

    private fun recycleViews(recycler: RecyclerView.Recycler, dx: Int) {
        for (i in 0 until itemCount) {
            val childView = getChildAt(i) ?: return
            //左滑
            if (dx > 0) {
                if (childView.right - dx < 0) {
                    removeAndRecycleViewAt(i, recycler)
                }
            } else { //右滑
                if (childView.left - dx > width) {
                    removeAndRecycleViewAt(i, recycler)
                }
            }
        }
    }

    private fun layout(recycler: RecyclerView.Recycler) {

        detachAndScrapAttachedViews(recycler)
        val currentPosition = findCurrentPosition()
        val left = currentPosition - 1
        val right = currentPosition + 1

        for (i in left..right) {
            val position = abs(i) % itemCount
            val scrapView = recycler.getViewForPosition(position)
            measureChildWithMargins(scrapView, 0, 0)
            resetViewProperty(scrapView)
            val targetOffset = position * mItemGap - mOffset
            layoutItem(scrapView, targetOffset)
            addView(scrapView, 0)

        }
    }

    private fun layoutItem(scrapView: View, targetOffset: Float) {
        layoutDecorated(
            scrapView,
            mRemainWidth + targetOffset.toInt(),
            mSpaceInVertical,
            mRemainWidth + mDecoratedMeasurementWidth,
            mSpaceInVertical + mDecoratedMeasurementHeight
        )
        scaleItem(scrapView, targetOffset)
    }

    private fun scaleItem(scrapView: View, targetOffset: Float) {
        val scale = calculateScale(targetOffset + mRemainWidth)
        scrapView.scaleX = scale
        scrapView.scaleY = scale
    }

    private fun resetViewProperty(v: View) {
        v.scaleX = 1f
        v.scaleY = 1f
    }

    private fun calculateScale(x: Float): Float {
        val xOffset = abs(x - (mOrientationHelper.totalSpace - mDecoratedMeasurementWidth) / 2F)
        val temp =
            if ((mDecoratedMeasurementWidth - xOffset) > 0) mDecoratedMeasurementWidth - xOffset else 1F
        return (mScale - 1F) / temp * mDecoratedMeasurementWidth + 1
    }


    private fun findCurrentPosition(): Int {
        if (mItemGap == 0F) return 0
        return (mOffset / mItemGap).roundToInt()
    }

    private fun getTotalSpace(): Int {
        return height - paddingTop - paddingBottom
    }

    private fun initItemGap() {
        mItemGap = mDecoratedMeasurementWidth * ((mScale - 1) / 2 + 1) + 30
    }

    fun getOffsetToCenter(): Int {
        return (findCurrentPosition() * mItemGap - mOffset).toInt()
    }

    fun getCurrentPosition(): Int {
        if (itemCount == 0) return 0
        var position = findCurrentPosition()

        position = if (position >= 0) {
            position % itemCount
        } else {
            itemCount + position % itemCount
        }

        return if (position == itemCount) 0 else position
    }

    fun getMaxOffset(): Float = (itemCount - 1) * mItemGap

    fun getMinOffset(): Float = 0F

    fun setOnPageChangeListener(onBannerPageChangeListener: OnBannerPageChangeListener) {
        this.onBannerPageChangeListener = onBannerPageChangeListener
    }
}