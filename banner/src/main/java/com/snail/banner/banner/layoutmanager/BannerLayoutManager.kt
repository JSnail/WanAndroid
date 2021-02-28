package com.snail.banner.banner.layoutmanager

import android.graphics.PointF
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.snail.banner.banner.listener.OnBannerPageChangeListener
import kotlin.math.abs
import kotlin.math.roundToInt

open class BannerLayoutManager : RecyclerView.LayoutManager(),
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
    private val mScale = 1.3F

    private val mOrientationHelper by lazy {
        OrientationHelper.createOrientationHelper(
            this,
            RecyclerView.HORIZONTAL
        )
    }

    var onBannerPageChangeListener: OnBannerPageChangeListener? = null


    init {
        isItemPrefetchEnabled = false
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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

    override fun canScrollHorizontally(): Boolean {
        return true
    }


    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val offsetPosition = getOffsetToPosition(position)
        recyclerView.smoothScrollBy(offsetPosition, 0, null)
        if (position == itemCount) {
            mOffset = -mItemGap
        }
    }

    private fun getOffsetToPosition(position: Int): Int {
        return ((findCurrentPosition() + (position - getCurrentPosition())) * mItemGap - mOffset).toInt()
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            mOffset = 0f
            return
        }

        val scrap = recycler.getViewForPosition(0)
        measureChildWithMargins(scrap, 0, 0)
        mDecoratedMeasurementWidth = mOrientationHelper.getDecoratedMeasurement(scrap)
        mDecoratedMeasurementHeight = mOrientationHelper.getDecoratedMeasurementInOther(scrap)
        mRemainWidth = (mOrientationHelper.totalSpace - mDecoratedMeasurementWidth) / 2
        mSpaceInVertical = (totalSpaceInOther - mDecoratedMeasurementHeight) / 2

        initItemGap()

        layoutItems(recycler)
    }


    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ): Int {
        return scrollBy(dx, recycler)
    }

    private fun scrollBy(dy: Int, recycler: RecyclerView.Recycler): Int {
        if (childCount == 0 || dy == 0) {
            return 0
        }
        mOffset += dy.toFloat()

        layoutItems(recycler)
        recycleViews(recycler, dy)
        return dy
    }

    private fun layoutItems(recycler: RecyclerView.Recycler) {
        if (itemCount == 0) return

        detachAndScrapAttachedViews(recycler)

        val currentPos = findCurrentPosition()
        val start = currentPos - 1
        val end = currentPos + 2

        for (i in start..end) {
            var adapterPosition = i
            if (i >= itemCount) {
                adapterPosition %= itemCount
            } else if (i < 0) {
                var delta = -adapterPosition % itemCount
                if (delta == 0) delta = itemCount
                adapterPosition = itemCount - delta
            }

            val scrap = recycler.getViewForPosition(adapterPosition)
            measureChildWithMargins(scrap, 0, 0)
            resetViewProperty(scrap)
            val targetOffset = i * mItemGap - mOffset
            layoutScrap(scrap, targetOffset)
            addView(scrap, 0)
        }
    }

    private fun layoutScrap(scrapView: View, targetOffset: Float) {
        layoutDecorated(
            scrapView,
            mRemainWidth + targetOffset.toInt(),
            mSpaceInVertical,
            mRemainWidth + mDecoratedMeasurementWidth + targetOffset.toInt(),
            mSpaceInVertical + mDecoratedMeasurementHeight
        )
        scaleView(scrapView, targetOffset)
    }

    private fun scaleView(itemView: View, targetOffset: Float) {
        val scale = calculateScale(targetOffset + mRemainWidth)
        itemView.scaleX = scale
        itemView.scaleY = scale
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

    private fun resetViewProperty(v: View) {
        v.scaleX = 1F
        v.scaleY = 1F
    }

    private fun calculateScale(x: Float): Float {
        val deltaX = abs(x - (mOrientationHelper.totalSpace - mDecoratedMeasurementWidth) / 2F)
        val diff =
            if (mDecoratedMeasurementWidth - deltaX > 0) mDecoratedMeasurementWidth - deltaX else 1F
        return (mScale - 1f) / mDecoratedMeasurementWidth * diff + 1
    }

    private fun findCurrentPosition(): Int {
        if (mItemGap == 0f) return 0
        return (mOffset / mItemGap).roundToInt()
    }


    private val totalSpaceInOther: Int
        get() = (height - paddingTop
                - paddingBottom)


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