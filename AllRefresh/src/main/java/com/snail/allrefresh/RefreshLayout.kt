package com.snail.allrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.snail.allrefresh.config.RefreshContentEnum
import com.snail.allrefresh.config.Style
import com.snail.allrefresh.listener.OnLoadMoreListener
import com.snail.allrefresh.listener.OnRefreshListener
import com.snail.allrefresh.listener.OnStartDownListener
import com.snail.allrefresh.listener.OnStartUpListener
import kotlin.math.abs

class RefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {


    //  默认刷新时间
    private val DEFAULT_DURATION = 300

    //  自动刷新时间
    private val DEFAULT_AUTO_DURATION = 100

    //    默认摩擦系数
    private val DEFAULT_FRICTION = 0.5f

    //   刷新完成时，默认平滑滚动单位距离  除CLASSIC外有效
    private val DEFAULT_SMOOTH_LENGTH = 50

    //   刷新完成时，默认平滑滚动单位时间 除CLASSIC外有效
    private val DEFAULT_SMOOTH_DURATION = 3


    //  通过触摸判断滑动方向
    private val NO_SCROLL = 0
    private val NO_SCROLL_REFRESH = 1
    private val NO_SCROLL_LOAD_MORE = 2


    //  头部
    private var mHeaderView: View? = null

    //  底部
    private var mFooterView: View? = null

    //    内容
    private var mContentView: View? = null

    //    列表  mIsCoo=true时有效
    private var mScrollView: View? = null

    private var mViewPager: ViewPager? = null
    private var mViewPager2: ViewPager2? = null

    private var mCurrentViewType = RefreshContentEnum.ERROR


    private var mAppBar: AppBarLayout? = null

    //   最大上拉高度 为0表示不限制
    private var mMaxHeaderHeight = 0

    //   最大下拉高度 为0表示不限制
    private var mMaxFooterHeight = 0

    //   开始下拉
    private var onStartUpListener: OnStartUpListener? = null

    //   开始上拉
    private var onStartDownListener: OnStartDownListener? = null

    //    头部高度
    private var mHeaderHeight = 0

    //    底部高度
    private var mFooterHeight = 0

    private val isSetHeaderHeight = false
    private val isSetFooterHeight = false

    //    是否在刷新中
    private var isHeaderRefreshing = false
    private var isFooterLoading = false

    //    摩擦系数
    private var mFriction = DEFAULT_FRICTION

    //    是否可下拉
    var mRefreshEnabled = true

    //    是否可上拉
    var mLoadMoreEnabled = true

    //   下拉监听
    private var mOnRefreshListener: OnRefreshListener? = null

    //    上拉监听
    private var mOnLoadMoreListener: OnLoadMoreListener? = null

    /**
     *头部view风格
     **/
    private var mHeadStyle = Style.NORMAL

    /**
     *底部风格
     **/
    private var mFooterStyle = Style.NORMAL

    //    刷新时间
    private var mDuration = DEFAULT_DURATION

    //    平滑滚动单位距离  除CLASSIC外有效
    private var mSmoothLength = DEFAULT_SMOOTH_LENGTH

    //    平滑滚动单位时间 除CLASSIC外有效
    private var mSmoothDuration = DEFAULT_SMOOTH_DURATION

    //  不可滑动view的滑动方向
    private var isUpOrDown = NO_SCROLL.toInt()

    //  判断y轴方向的存储值
    private var directionX = 0f

    //   判断x轴方向存储值
    private var directionY = 0f

    //   下拉偏移
    private var mHeadOffY = 0

    //    上拉偏移
    private var mFootOffY = 0

    //    内容偏移
    private var mContentOffY = 0

    //  最后一次触摸的位置
    private var lastY = 0f

    //  偏移
    private var currentOffSetY = 0

    //  触摸移动的位置
    private var offsetSum = 0

    //    触摸移动的位置之和
    private var scrollSum = 0

    //  一个缓存值
    private var tempY = 0

    /**
     *    下拉刷新时背景
     **/
    private var mRefreshBackgroundResource = 0
    private var mRefreshBackgroundColor = 0

    /**
     *上拉加载更多的背景
     **/
    private var mLoadMoreBackgroundResource = 0
    private var mLoadMoreBackgroundColor = 0

    //  内容视图是否是CoordinatorLayout
    private var mIsCoo = false

    //  是否展开  mIsCoo=true时有效
    private var isSpreading = true

    private val mScroller = Scroller(context)

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout, defStyleAttr, 0)
        mRefreshEnabled = type.getBoolean(R.styleable.RefreshLayout_isCanRefresh, true)
        mLoadMoreEnabled = type.getBoolean(R.styleable.RefreshLayout_isCanLoadMore, true)
        mHeadStyle = type.getInt(R.styleable.RefreshLayout_headStyle, Style.NORMAL)
        mFooterStyle = type.getInt(R.styleable.RefreshLayout_footerStyle, Style.NORMAL)
        mFriction = type.getFloat(R.styleable.RefreshLayout_friction, 0.5F)
        mDuration = type.getInt(R.styleable.RefreshLayout_duration, 300)
        mSmoothLength = type.getInt(R.styleable.RefreshLayout_smoothLength, 0)
        mSmoothDuration =
            type.getInt(R.styleable.RefreshLayout_smoothDuration, 3)
        mRefreshBackgroundResource = type.getResourceId(
            R.styleable.RefreshLayout_backgroundRefresh,
            android.R.color.transparent
        )
        mIsCoo = type.getBoolean(R.styleable.RefreshLayout_isCoo, false)

        type.recycle()
    }

    override fun onFinishInflate() {
        if (childCount > 0) {
            mHeaderView = findViewById(R.id.headerView)
            mContentView = findViewById(R.id.contentView)
            mScrollView = findViewById(R.id.scrollView)
            mFooterView = findViewById(R.id.footerView)
            checkView()
        } else {
            throw NullPointerException("Refresh don‘t have any view")
        }
        super.onFinishInflate()
    }

    private fun checkView() {
        checkNotNull(mContentView) { "mContentView can not be null" }
        if (mIsCoo) {
            if (mContentView is CoordinatorLayout) {
                mAppBar = (mContentView as CoordinatorLayout).getChildAt(0) as AppBarLayout
                initAppBarLayout()
            } else {
                throw  IllegalArgumentException("mContentView is not CoordinatorLayout ")
            }

            if (null == mScrollView) {
                throw IllegalStateException("mScrollView is null")
            }

            mCurrentViewType = when (mScrollView) {
                is ViewPager -> {
                    mViewPager = mScrollView as ViewPager
                    RefreshContentEnum.VIEWPAGER
                }
                is ViewPager2 -> {
                    mViewPager2 = mScrollView as ViewPager2
                    RefreshContentEnum.VIEWPAGER2
                }
                is NestedScrollingChild -> RefreshContentEnum.NESTEDSCROLLINGCHILD
                else -> RefreshContentEnum.ERROR
            }
            if (mCurrentViewType == RefreshContentEnum.ERROR) {
                throw  IllegalArgumentException("mScrollView is not viewPager,viewPager or NestedScrollingChild")
            }
        }

        mHeaderView?.let {
            if (mHeaderView !is RefreshInterface) {
                throw IllegalArgumentException("mHeaderView error")
            }
            getHeaderInterface().setIsHeaderOrFooter(true)
        }
        mFooterView?.let {
            if (mFooterView !is RefreshInterface) {
                throw IllegalArgumentException("mFooterView error")
            }
            getFooterInterface().setIsHeaderOrFooter(false)
        }

        setStyle(mHeadStyle, mFooterStyle)
    }

    fun setStyle(@Style mHeadStyle: Int, @Style mFooterStyle: Int) {
        this.mHeadStyle = mHeadStyle
        this.mFooterStyle = mFooterStyle

//        if (mHeadStyle == Style.NORMAL || mFooterStyle == Style.NORMAL) {
//            bringChildToFront(mContentView)
//        }

        if (mHeadStyle == Style.IN_ABOVE) {
            bringChildToFront(mHeaderView)
        }

        if (mFooterStyle == Style.IN_ABOVE) {
            bringChildToFront(mFooterView)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeaderView?.let {
            measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val params = it.layoutParams as MarginLayoutParams
            if (!isSetHeaderHeight) {
                mHeaderHeight = params.bottomMargin + params.topMargin + it.measuredHeight
            }
        }
        mFooterView?.let {
            measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val params = it.layoutParams as MarginLayoutParams
            if (!isSetFooterHeight) {
                mFooterHeight = params.bottomMargin + params.topMargin + it.measuredHeight
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutChild()
    }

    private fun layoutChild() {

        mHeaderView?.let {
            val params = it.layoutParams as MarginLayoutParams
            val left = paddingLeft + params.leftMargin
            val top = paddingTop + params.topMargin + mHeadOffY - mHeaderHeight
            val right = left + it.measuredWidth
            val bottom = top + it.measuredHeight
            it.layout(left, top, right, bottom)
        }

        mFooterView?.let {
            val params = it.layoutParams as MarginLayoutParams
            val left = paddingLeft + params.leftMargin
            val top = measuredHeight + paddingTop + params.topMargin - mFootOffY
            val right = left + it.measuredWidth
            val bottom = top + it.measuredHeight
            it.layout(left, top, right, bottom)
        }

        mContentView?.let {
            val params = it.layoutParams as MarginLayoutParams
            val left = paddingLeft + params.leftMargin
            val top = paddingTop + params.topMargin + mContentOffY
            val right = left + it.measuredWidth
            val bottom = top + it.measuredHeight
            it.layout(left, top, right, bottom)
        }

    }

    private fun initAppBarLayout() {
        mAppBar?.let {
            it.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val minHeight = appBarLayout.measuredHeight / 2
                if (verticalOffset == 0) {
                    isSpreading = false
                } else if (abs(verticalOffset) > minHeight) {
                    isSpreading = true
                }
            })
        }
    }

    private fun getHeaderInterface(): RefreshInterface {
        return mHeaderView as RefreshInterface
    }

    private fun getFooterInterface(): RefreshInterface {
        return mFooterView as RefreshInterface
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                directionX = ev.x
                directionY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (directionY > 0 && directionX > 0) {
                    val eventX = ev.x
                    val eventY = ev.y

                    val offsetX = eventX - directionX
                    val offsetY = eventY - directionY

                    directionX = eventX
                    directionY = eventY

                    val moved = abs(offsetY) > abs(offsetX)
                    isUpOrDown = if (offsetY > 0 && moved && canRefresh()) {
                        NO_SCROLL_REFRESH
                    } else if (offsetY < 0 && moved && canLoadMore()) {
                        NO_SCROLL_LOAD_MORE
                    } else {
                        NO_SCROLL
                    }
                    if (isUpOrDown == NO_SCROLL_LOAD_MORE || isUpOrDown == NO_SCROLL_REFRESH) {
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //不可滑动的view
        if (!contentCanScroll(true) || !contentCanScroll(false)) {
            when (isUpOrDown) {
                NO_SCROLL_REFRESH -> if (canRefresh()) {
                    return touch(event, true)
                }
                NO_SCROLL_LOAD_MORE -> {
                    return touch(event, false)
                }
                else -> {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            directionX = event.x
                            directionY = event.y
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (directionX > 0 && directionY > 0) {
                                val eventX = event.x
                                val eventY = event.y

                                val offsetX = eventX - directionX
                                val offsetY = eventY - directionY

                                directionX = eventX
                                directionY = eventY

                                val moved = abs(offsetY) > abs(offsetX)
                                isUpOrDown = if (offsetY > 0 && moved && canRefresh()) {
                                    NO_SCROLL_REFRESH
                                } else if (offsetY < 0 && moved && canLoadMore()) {
                                    NO_SCROLL_LOAD_MORE
                                } else {
                                    NO_SCROLL
                                }

                            }
                        }
                    }
                    return true
                }
            }
        } else {
            if (canRefresh()) {
                return touch(event, true)
            } else if (canLoadMore()) {
                return touch(event, false)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun touch(e: MotionEvent, isHead: Boolean): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                lastY = e.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                move(e, isHead)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                finish(isHead)
                return true
            }
        }
        return super.onTouchEvent(e)
    }

    private fun move(e: MotionEvent, isHead: Boolean) {
        if (lastY > 0) {
            currentOffSetY = (e.y - lastY).toInt()
            offsetSum += currentOffSetY
        }
        lastY = e.y

        val canMove = if (isHead) offsetSum > 0 else offsetSum < 0

        if (canMove) {
            var factor = getFactor()
            if (factor < 0) factor = 0F
            var scrollNum = -(currentOffSetY * factor).toInt()
            scrollSum += scrollNum

            if (isHead) {
                if (mMaxHeaderHeight > 0 && abs(scrollSum) > mMaxHeaderHeight) {
                    scrollSum = if (scrollSum > 0) mMaxHeaderHeight else -mMaxHeaderHeight
                    scrollNum = 0
                }
                setBackgroundResource(true)
                onStartUpListener?.let {
                    if (abs(scrollSum) > 0) {
                        it.onUp()
                    }
                }
                smoothMove(isHead = true, isMove = true, moveScrollY = scrollNum, moveY = scrollSum)

                if (abs(scrollSum) > mHeaderHeight) {
                    getHeaderInterface().onPrepare()
                }
                getHeaderInterface().onPositionChange(abs(scrollSum) / mHeaderHeight.toFloat())

            } else {
                if (mMaxFooterHeight > 0 && abs(scrollNum) > mMaxFooterHeight) {
                    scrollSum = if (scrollSum > 0) mMaxFooterHeight else -mMaxFooterHeight
                    scrollNum = 0
                }
                setBackgroundResource(false)
                onStartDownListener?.let {
                    if (abs(scrollSum) > 0) {
                        it.onDown()
                    }
                }
                smoothMove(
                    isHead = false,
                    isMove = true,
                    moveScrollY = scrollNum,
                    moveY = scrollSum
                )

                if (abs(scrollSum) > mFooterHeight) {
                    getFooterInterface().onPrepare()
                }
                getFooterInterface().onPositionChange(abs(scrollSum) / mFooterHeight.toFloat())
            }
        }
    }

    private fun finish(isHead: Boolean) {
        performClick()
        if (isHead) {
            //当滑动的距离大于等于头部高度的时候触发刷新操作
            if (abs(scrollSum) >= mHeaderHeight) {
                getHeaderInterface().onRelease()
                smoothMove(
                    isHead = true,
                    isMove = false,
                    moveScrollY = if (mHeadStyle == Style.NORMAL) -mHeaderHeight else mHeaderHeight,
                    moveY = mHeaderHeight
                )
                refresh()
            } else {
                getHeaderInterface().onReleaseNoEnough(abs(scrollSum) / mHeaderHeight.toFloat())
                smoothMove(true, false, 0, 0)
                onStartUpListener?.onReset()
            }
        } else {
            if (abs(scrollSum) >= mFooterHeight) {
                getFooterInterface().onRelease()
                smoothMove(
                    isHead = false,
                    isMove = false,
                    moveScrollY = if (mFooterStyle == Style.NORMAL) (mContentView?.measuredHeight
                        ?: 0 + mFooterHeight - measuredHeight) else mFooterHeight,
                    moveY = mFooterHeight
                )
                loadMore()
            } else {
                getFooterInterface().onReleaseNoEnough(abs(scrollSum) / mFooterHeight.toFloat())
                smoothMove(
                    isHead = false,
                    isMove = false,
                    moveScrollY = if (mFooterStyle == Style.NORMAL) (mContentView?.measuredHeight
                        ?: 0 + mFooterHeight) else 0, moveY = 0
                )
                onStartDownListener?.onReset()
            }
        }
        resetParameter()
    }

    /**
     * 重置参数
     */
    private fun resetParameter() {
        directionX = 0f
        directionY = 0f
        isUpOrDown = NO_SCROLL
        lastY = 0f
        offsetSum = 0
        scrollSum = 0
    }

    private fun refresh() {
        isHeaderRefreshing = true
        mOnRefreshListener?.onRefresh()
    }

    private fun loadMore() {
        isFooterLoading = true
        mOnLoadMoreListener?.onLoadMore()
    }

    /**
     * * 滚动布局的方法
     *
     * @param isHead    boolean
     * @param isMove      boolean  手指在移动还是已经抬起
     * @param moveScrollY int
     * @param moveY       int
     */
    private fun smoothMove(isHead: Boolean, isMove: Boolean, moveScrollY: Int, moveY: Int) {
        val newMoveY = abs(moveY)
        if (mHeadStyle == Style.NORMAL || mFooterStyle == Style.NORMAL) {
            if (isMove) {
                smoothScrollBy(0, moveScrollY)
            } else {
                smoothScrollTo(0, moveScrollY)
            }
        } else {
            calculateOffset(isHead, isMove, moveScrollY, newMoveY)
        }
    }

    /**
     * 调用此方法滚动到目标位置
     */
    private fun smoothScrollTo(fx: Int, fy: Int) {
        val dx = fx - mScroller.finalX
        val dy = fy - mScroller.finalY
        smoothScrollBy(dx, dy)
    }

    /**
     * 调用此方法设置滚动的相对偏移
     */
    private fun smoothScrollBy(dx: Int, dy: Int) {
        mScroller.startScroll(mScroller.finalX, mScroller.finalY, dx, dy)
        invalidate()
    }

    /**
     *计算在不同风格下的头部移动距离
     **/
    private fun calculateOffset(
        isHead: Boolean,
        isMove: Boolean,
        moveScrollY: Int,
        moveY: Int
    ) {
        if (isMove) {
            if (isHead) {
                when (mHeadStyle) {
                    Style.IN_ABOVE -> mHeadOffY = moveY
                    Style.IN_LOWER -> {
                        mHeadOffY = mHeaderHeight
                        mContentOffY = moveY
                    }
                }
            } else {
                when (mFooterStyle) {
                    Style.IN_ABOVE -> mFootOffY = moveY
                    Style.IN_LOWER -> {
                        mFootOffY = mHeaderHeight
                        mContentOffY = -moveY
                    }
                }
            }
        } else {
            smoothLayout(isHead, moveScrollY, moveY)
        }
        requestLayout()
    }

    private fun smoothLayout(isHead: Boolean, moveScrollY: Int, moveY: Int) {
        tempY = when (moveScrollY > 0) {
            true -> moveScrollY
            false -> abs(scrollSum)
        }
        tempY -= mSmoothLength
        if (tempY < moveY) {
            calculateOffset(isHead, true, moveScrollY, moveY)
            return
        }
        calculateOffset(isHead, true, moveScrollY, tempY)

        postDelayed({
            smoothLayout(isHead, moveScrollY, moveY)
        }, mSmoothDuration.toLong())
    }

    private fun setBackgroundResource(isRefresh: Boolean) {
        if (isRefresh) {
            if (mRefreshBackgroundColor != 0) {
                setBackgroundColor(mRefreshBackgroundColor)
            } else {
                setBackgroundResource(mRefreshBackgroundResource)
            }
        } else {
            if (mLoadMoreBackgroundColor != 0) {
                setBackgroundColor(mLoadMoreBackgroundColor)
            } else {
                setBackgroundResource(mLoadMoreBackgroundResource)
            }
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
        super.computeScroll()

    }

    fun refreshComplete() {
        if (!isHeaderRefreshing) {
            return
        }
        postDelayed({
            smoothMove(
                true,
                isMove = false,
                moveScrollY = if (mHeadStyle == Style.NORMAL) 0 else mHeaderHeight,
                moveY = 0
            )
            getHeaderInterface().run {
                this.onComplete()
                this.onReset()
            }
            isHeaderRefreshing = false
            onStartUpListener?.onReset()
        }, mDuration.toLong())
    }

    fun lordMoreComplete() {
        if (!isFooterLoading) {
            return
        }
        postDelayed({
            smoothMove(
                isHead = false,
                isMove = false,
                moveScrollY = if (mFooterStyle == Style.NORMAL) mContentView?.measuredHeight
                    ?: 0 - measuredHeight else mFooterHeight,
                moveY = 0
            )
            getFooterInterface().run {
                this.onComplete()
                this.onReset()
            }
            isFooterLoading = false
            onStartDownListener?.onReset()
        }, mDuration.toLong())
    }

    fun autoRefresh() {
        mHeaderView?.let {
            postDelayed({
                setBackgroundResource(true)
                smoothMove(
                    isHead = true,
                    isMove = false,
                    moveScrollY = -mHeaderHeight,
                    moveY = -mHeaderHeight
                )
                getHeaderInterface().onRelease()
                refresh()
            }, DEFAULT_AUTO_DURATION.toLong())
        }

    }


    /**
     * 滑动距离越大比率越小，越难拖动
     *
     * @return float
     */
    private fun getFactor(): Float {
        return 1 - abs(offsetSum) / measuredHeight.toFloat() - 0.3f * mFriction
    }

    /**
     * @param isDown  true 是否是向下滑动
     **/
    private fun contentCanScroll(isDown: Boolean): Boolean {
        if (mIsCoo) {
            when (mCurrentViewType) {
                RefreshContentEnum.VIEWPAGER -> {
                    mViewPager?.let {
                        val current = it.currentItem
                        if (current < it.childCount) {
                            val adapter = it.adapter
                            mScrollView = if (adapter is FragmentPagerAdapter) {
                                adapter.getItem(current).view
                            } else {
                                it.getChildAt(current)
                            }
                        }
                    }
                }
                RefreshContentEnum.VIEWPAGER2 -> {
                    mViewPager2?.let {
                        val current = it.currentItem
                        if (current < it.childCount) {
                            val adapter = it.adapter
                            mScrollView = if (adapter is FragmentStateAdapter) {
                                adapter.createFragment(current).view
                            } else {
                                it.getChildAt(current)
                            }
                        }
                    }
                }
                else -> {
                    if (null == mScrollView) return false
                }
            }
            return if (isDown) {
                isSpreading || canScrollDown(mScrollView)
            } else {
                !isSpreading || canScrollUp(mScrollView)
            }
        }
        return if (isDown) {
            canScrollUp(mContentView)
        } else {
            canScrollDown(mContentView)
        }
    }

    private fun canScrollDown(view: View?): Boolean {
        return view!!.canScrollVertically(1)
    }

    private fun canScrollUp(view: View?): Boolean {
        return view!!.canScrollVertically(-1)
    }

    /**
     * 能否刷新
     *
     * @return boolean
     */
    private fun canRefresh(): Boolean {
        return !isHeaderRefreshing && mRefreshEnabled && mHeaderView != null && !contentCanScroll(
            true
        )
    }

    /**
     * 能否加载更多
     *
     * @return boolean
     */
    private fun canLoadMore(): Boolean {
        return !isFooterLoading && mLoadMoreEnabled && mFooterView != null && !contentCanScroll(
            false
        )
    }

    fun addOnLoadMoreListener(listener: () -> Unit): RefreshLayout {
        this.mOnLoadMoreListener = object : OnLoadMoreListener {
            override fun onLoadMore() {
                listener.invoke()
            }
        }
        return this
    }

    fun addOnRefreshListener(listener: () -> Unit): RefreshLayout {
        this.mOnRefreshListener = object : OnRefreshListener {
            override fun onRefresh() {
                listener.invoke()
            }
        }
        return this
    }
}