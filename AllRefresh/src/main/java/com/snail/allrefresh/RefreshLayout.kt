package com.snail.allrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Scroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.NestedScrollingChild
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.snail.allrefresh.CanRefreshLayout.*
import com.snail.allrefresh.config.RefreshContentEnum
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
    private val NO_SCROLL: Byte = 0
    private val NO_SCROLL_UP: Byte = 1
    private val NO_SCROLL_DOWN: Byte = 2


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

    private var mCurrentViewPager = RefreshContentEnum.ERROR


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
    private val isHeaderRefreshing = false
    private val isFooterRefreshing = false


    private val mMidContentPara = 2.0f
    private val mMidHeaderPara = 2.0f
    private val mRefreshRatio = 1.0f

    //   滑动到多少时松手显示全部
    private val mRefreshUpRatio = 0f

    //    摩擦系数
    private var mFriction = DEFAULT_FRICTION

    //    是否可下拉
    var mRefreshEnabled = true


    //    是否可上拉
    var mCanLoadMore = true

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
    var directionX = 0f

    //   判断x轴方向存储值
    var directionY = 0f

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
    var mRefreshBackgroundResource = 0
    private var mRefreshBackgroundColor = 0

    /**
     *上拉加载更多的背景
     **/
    var mLoadMoreBackgroundResource = 0
    private var mLoadMoreBackgroundColor = 0

    //  内容视图是否是CoordinatorLayout
    private var mIsCoo = false

    //  是否展开  mIsCoo=true时有效
    private var isSpreading = true

    private val mScroller = Scroller(context)

    init {
        val type = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout, defStyleAttr, 0)
        mRefreshEnabled = type.getBoolean(R.styleable.RefreshLayout_isCanRefresh, true)
        mCanLoadMore = type.getBoolean(R.styleable.RefreshLayout_isCanLoadMore, true)
        mHeadStyle = type.getInt(R.styleable.RefreshLayout_headStyle, Style.NORMAL)
        mFooterStyle = type.getInt(R.styleable.RefreshLayout_footerStyle, Style.NORMAL)
        mFriction = type.getFloat(R.styleable.RefreshLayout_friction, 0.5F)
        mDuration = type.getInt(R.styleable.RefreshLayout_duration, 300)
        mSmoothLength = type.getInt(R.styleable.RefreshLayout_smoothLength, 0)
        mSmoothDuration =
            type.getInt(R.styleable.RefreshLayout_smoothDuration, android.R.color.transparent)
        mRefreshBackgroundResource = type.getResourceId(
            R.styleable.RefreshLayout_backgroundRefresh,
            android.R.color.transparent
        )
        mIsCoo = type.getBoolean(R.styleable.RefreshLayout_isCoo, false)

        type.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            mHeaderView = findViewById(R.id.header_view)
            mContentView = findViewById(R.id.content_view)
            mScrollView = findViewById(R.id.scroll_view)
            mFooterView = findViewById(R.id.footer_view)
            checkView()
        } else {
            throw NullPointerException("Refresh don‘t have any view")
        }
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

            mCurrentViewPager = when (mScrollView) {
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
            if (mCurrentViewPager == RefreshContentEnum.ERROR) {
                throw  IllegalArgumentException("mScrollView is not viewPager,viewPager or NestedScrollingChild")
            }
        }

        mHeaderView?.let {
            if (mHeaderView !is RefreshLayout) {
                throw IllegalArgumentException("mHeaderView error")
            }
            getHeaderInterface().setIsHeaderOrFooter(true)
        }
        mFooterView?.let {
            if (mFooterView !is RefreshLayout) {
                throw IllegalArgumentException("mFooterView error")
            }
            getFooterInterface().setIsHeaderOrFooter(false)
        }

        setStyle(mHeadStyle, mFooterStyle)
    }

    fun setStyle(@Style mHeadStyle: Int, @Style mFooterStyle: Int) {
        this.mHeadStyle = mHeadStyle
        this.mFooterStyle = mFooterStyle

        if (mHeadStyle == Style.NORMAL || mFooterStyle == Style.NORMAL) {
            bringChildToFront(mContentView)
        }

        if (mHeadStyle == Style.IN_UP) {
            bringChildToFront(mHeaderView)
        }

        if (mFooterStyle == Style.IN_UP) {
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when(it){

            }
        }
        return super.onTouchEvent(event)
    }
}