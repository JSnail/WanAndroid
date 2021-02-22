package com.snail.banner.banner.layoutmanager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


/**
 * @author wenshi
 * @github
 * @Description
 * @since 2019/6/26
 */
class StackLayoutManager @JvmOverloads constructor(context: Context, gap: Int = 0) :
    RecyclerView.LayoutManager() {
    /**
     * 一次完整的聚焦滑动所需要的移动距离
     */
    private var onceCompleteScrollLength = -1f

    /**
     * 第一个子view的偏移量
     */
    private var firstChildCompleteScrollLength = -1f

    /**
     * 屏幕可见第一个view的position
     */
    private var mFirstVisiPos = 0

    /**
     * 屏幕可见的最后一个view的position
     */
    private var mLastVisiPos = 0

    /**
     * 水平方向累计偏移量
     */
    private var mHorizontalOffset: Long = 0

    /**
     * 普通view之间的margin
     */
    private var normalViewGap = 30f
    private var childWidth = 0

    /**
     * 是否自动选中
     */
    private val isAutoSelect = true
    private var selectAnimator: ValueAnimator? = null
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        //  super.onLayoutChildren(recycler, state);
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        onceCompleteScrollLength = -1f

        // 分离全部已有的view 放入临时缓存  mAttachedScrap 集合中
        detachAndScrapAttachedViews(recycler)
        fill(recycler, state, 0)
    }

    private fun fill(recycler: Recycler, state: RecyclerView.State, dx: Int): Int {
        var resultDelta = dx
        resultDelta = fillHorizontalLeft(recycler, state, dx)
        recycleChildren(recycler)
        return resultDelta
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        // 手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
        // 位移0、没有子View 当然不移动
        var dx = dx
        if (dx == 0 || childCount == 0) {
            return 0
        }
        val realDx = dx / 1.0f
        if (Math.abs(realDx) < 0.00000001f) {
            return 0
        }
        mHorizontalOffset += dx.toLong()
        dx = fill(recycler, state, dx)
        offsetChildrenHorizontal(-dx)
        return dx
    }

    /**
     * 最大偏移量
     *
     * @return
     */
    private val maxOffset: Float
        private get() = if (childWidth == 0 || itemCount == 0) 0F else (childWidth + normalViewGap) * (itemCount - 1)
    // getWidth() / 2 + childWidth / 2 +

    /**
     * 获取最小的偏移量
     *
     * @return
     */
    private val minOffset: Float
        private get() = if (childWidth == 0) 0F else ((width - childWidth) / 2).toFloat()

    private fun fillHorizontalLeft(recycler: Recycler, state: RecyclerView.State, dx: Int): Int {
        //----------------1、边界检测-----------------
        var dx = dx
        if (dx < 0) {
            // 已到达左边界
            if (mHorizontalOffset < 0) {
//                dx = 0
                mHorizontalOffset = dx.toLong()
            }
        }
        if (dx > 0) {
            if (mHorizontalOffset >= maxOffset) {
                // 因为在因为scrollHorizontallyBy里加了一次dx，现在减回去
                // mHorizontalOffset -= dx;
                mHorizontalOffset = maxOffset.toLong()
//                dx = 0
            }
        }

        // 分离全部的view，加入到临时缓存
        detachAndScrapAttachedViews(recycler)
        var startX = 0f
        var fraction = 0f
        var isChildLayoutLeft = true
        var tempView: View? = null
        var tempPosition = -1
        if (onceCompleteScrollLength == -1f) {
            // 因为mFirstVisiPos在下面可能被改变，所以用tempPosition暂存一下
            tempPosition = mFirstVisiPos
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            childWidth = getDecoratedMeasurementHorizontal(tempView)
        }

        // 修正第一个可见view mFirstVisiPos 已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        firstChildCompleteScrollLength = (width / 2 + childWidth / 2).toFloat()
        if (mHorizontalOffset >= firstChildCompleteScrollLength) {
            startX = normalViewGap
            onceCompleteScrollLength = childWidth + normalViewGap
            mFirstVisiPos =
                Math.floor((Math.abs(mHorizontalOffset - firstChildCompleteScrollLength) / onceCompleteScrollLength).toDouble())
                    .toInt() + 1
            fraction =
                Math.abs(mHorizontalOffset - firstChildCompleteScrollLength) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)
        } else {
            mFirstVisiPos = 0
            startX = minOffset
            onceCompleteScrollLength = firstChildCompleteScrollLength
            fraction =
                Math.abs(mHorizontalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)
        }

        // 临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = itemCount - 1
        val normalViewOffset = onceCompleteScrollLength * fraction
        var isNormalViewOffsetSetted = false

        //----------------3、开始布局-----------------
        for (i in mFirstVisiPos..mLastVisiPos) {
            val item: View = if (i == tempPosition && tempView != null) {
                // 如果初始化数据时已经取了一个临时view
                tempView
            } else {
                recycler.getViewForPosition(i)
            }
            val focusPosition = (Math.abs(mHorizontalOffset) / (childWidth + normalViewGap)).toInt()
            if (i <= focusPosition) {
                addView(item)
            } else {
                addView(item, 0)
            }
            measureChildWithMargins(item, 0, 0)
            if (!isNormalViewOffsetSetted) {
                startX -= normalViewOffset
                isNormalViewOffsetSetted = true
            }
            var r: Int
            var b: Int
            val l: Int = startX.toInt()
            val t: Int = paddingTop
            r = l + getDecoratedMeasurementHorizontal(item)
            b = t + getDecoratedMeasurementVertical(item)

            // 缩放子view
            val minScale = 0.6f
            var currentScale = 0f
            val childCenterX = (r + l) / 2
            val parentCenterX = width / 2
            isChildLayoutLeft = childCenterX <= parentCenterX
            currentScale = if (isChildLayoutLeft) {
                val fractionScale = (parentCenterX - childCenterX) / (parentCenterX * 1.0f)
                1.0f - (1.0f - minScale) * fractionScale
            } else {
                val fractionScale = (childCenterX - parentCenterX) / (parentCenterX * 1.0f)
                1.0f - (1.0f - minScale) * fractionScale
            }

            Log.i("TAG","childCenterX   ==  $childCenterX    currentScale == $currentScale   \n" +
                    "left == $l    right==$r")

            item.scaleX = currentScale
            item.scaleY = currentScale
            // item.setAlpha(currentScale);
            layoutDecoratedWithMargins(item, l, t, r, b)
            startX += childWidth + normalViewGap
            if (startX > width - paddingRight) {
                mLastVisiPos = i
                break
            }
        }
        return dx
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            RecyclerView.SCROLL_STATE_DRAGGING ->                 //当手指按下时，停止当前正在播放的动画
                cancelAnimator()
            RecyclerView.SCROLL_STATE_IDLE ->                 //当列表滚动停止后，判断一下自动选中是否打开
                if (isAutoSelect) {
                    //找到离目标落点最近的item索引
                    smoothScrollToPosition(findShouldSelectPosition(), null)
                }

        }
    }

    fun findShouldSelectPosition(): Int {
        if (onceCompleteScrollLength == -1f || mFirstVisiPos == -1) {
            return -1
        }
        val position = (Math.abs(mHorizontalOffset) / (childWidth + normalViewGap)).toInt()
        val remainder = (Math.abs(mHorizontalOffset) % (childWidth + normalViewGap)).toInt()
        // 超过一半，应当选中下一项
        if (remainder >= (childWidth + normalViewGap) / 2.0f) {
            if (position + 1 <= itemCount - 1) {
                return position + 1
            }
        }
        return position
    }

    /**
     * 平滑滚动到某个位置
     *
     * @param position 目标Item索引
     */
    fun smoothScrollToPosition(position: Int, listener: OnStackListener?) {
        if (position > -1 && position < itemCount) {
            startValueAnimator(position, listener)
        }
    }

    private fun startValueAnimator(position: Int, listener: OnStackListener?) {
        cancelAnimator()
        val distance = getScrollToPositionOffset(position)
        val minDuration: Long = 100
        val maxDuration: Long = 300
        val duration: Long
        val distanceFraction = Math.abs(distance) / (childWidth + normalViewGap)
        duration = if (distance <= childWidth + normalViewGap) {
            (minDuration + (maxDuration - minDuration) * distanceFraction).toLong()
        } else {
            (maxDuration * distanceFraction).toLong()
        }
        selectAnimator = ValueAnimator.ofFloat(0.0f, distance)
        selectAnimator?.setDuration(duration)
        selectAnimator?.setInterpolator(LinearInterpolator())
        val startedOffset = mHorizontalOffset.toFloat()
        selectAnimator?.addUpdateListener(AnimatorUpdateListener { animation ->
            val value = animation.animatedValue as Float
            mHorizontalOffset = (startedOffset + value).toLong()
            requestLayout()
        })
        selectAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                listener?.onFocusAnimEnd()
            }
        })
        selectAnimator?.start()
    }

    /**
     * @param position
     * @return
     */
    private fun getScrollToPositionOffset(position: Int): Float {
        return position * (childWidth + normalViewGap) - Math.abs(mHorizontalOffset)
    }

    /**
     * 取消动画
     */
    fun cancelAnimator() {
        if (selectAnimator != null && (selectAnimator!!.isStarted || selectAnimator!!.isRunning)) {
            selectAnimator!!.cancel()
        }
    }

    /**
     * 回收需回收的item
     */
    private fun recycleChildren(recycler: Recycler) {
        val scrapList = recycler.scrapList
        for (i in scrapList.indices) {
            val holder = scrapList[i]
            removeAndRecycleView(holder.itemView, recycler)
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin)
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin)
    }

    val verticalSpace: Int
        get() = height - paddingTop - paddingBottom
    val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight

    interface OnStackListener {
        fun onFocusAnimEnd()
    }

    companion object {
        fun dp2px(context: Context, dp: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                context.resources.displayMetrics
            )
        }
    }

    init {
        normalViewGap = dp2px(context, gap.toFloat())
    }
}