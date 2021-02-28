package com.snail.wanandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.snail.wanandroid.R

class MultiStateView : FrameLayout {
    enum class ViewState {
        CONTENT, LOADING, EMPTY, ERROR, CUSTOM
    }

    private lateinit var mInflater: LayoutInflater
    private var mContentView: View? = null
    private var mLoadingView: View? = null
    private var mErrorView: View? = null
    private var mEmptyView: View? = null
    private var mCustomView: View? = null
    private var mViewState = ViewState.CONTENT

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(
        context, attrs
    ) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mInflater = LayoutInflater.from(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)
        val loadingViewResId =
            a.getResourceId(R.styleable.MultiStateView_msv_loadingView, R.layout.view_loading)
        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false)
            addView(mLoadingView, mLoadingView?.layoutParams)
        }
        val emptyViewResId =
            a.getResourceId(R.styleable.MultiStateView_msv_emptyView, R.layout.view_empty)
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false)
            addView(mEmptyView, mEmptyView?.layoutParams)
        }
        val errorViewResId =
            a.getResourceId(R.styleable.MultiStateView_msv_errorView, R.layout.view_error)
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false)
            addView(mErrorView, mErrorView?.layoutParams)
        }
        val customViewResId = a.getResourceId(R.styleable.MultiStateView_msv_customView, -1)
        if (customViewResId > -1) {
            mCustomView = mInflater.inflate(customViewResId, this, false)
            addView(mCustomView, mCustomView?.layoutParams)
        }
        val viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, UNKNOWN_VIEW)
        create(viewState)
        a.recycle()
    }

    private fun create(viewState: Int) {
        if (viewState != UNKNOWN_VIEW) {
            when (viewState) {
                CONTENT_VIEW -> mViewState = ViewState.CONTENT
                ERROR_VIEW -> mViewState = ViewState.ERROR
                EMPTY_VIEW -> mViewState = ViewState.EMPTY
                LOADING_VIEW -> mViewState = ViewState.LOADING
                CUSTOM_VIEW -> mViewState = ViewState.CUSTOM
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requireNotNull(mContentView) { "Content view is not defined" }
        setView()
    }

    override fun addView(child: View?) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, index)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, index, params)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, params)
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(
        child: View,
        index: Int,
        params: ViewGroup.LayoutParams,
        preventRequestLayout: Boolean
    ): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    fun getView(state: ViewState?): View? {
        return when (state) {
            ViewState.LOADING -> mLoadingView
            ViewState.CONTENT -> mContentView
            ViewState.EMPTY -> mEmptyView
            ViewState.ERROR -> mErrorView
            ViewState.CUSTOM -> mCustomView
            else -> null
        }
    }

    /**
     * @return
     */
    var viewState: ViewState
        get() = mViewState
        set(state) {
            if (state != mViewState) {
                mViewState = state
                setView()
            }
        }

    private fun setView() {
        when (mViewState) {
            ViewState.LOADING -> {
                if (mLoadingView == null) {
                    throw NullPointerException("Loading View")
                }
                mLoadingView?.visibility = VISIBLE
                if (mContentView != null) mContentView?.visibility = GONE
                if (mErrorView != null) mErrorView?.visibility = GONE
                if (mEmptyView != null) mEmptyView?.visibility = GONE
                if (mCustomView != null) mCustomView?.visibility = GONE
            }
            ViewState.EMPTY -> {
                if (mEmptyView == null) {
                    throw NullPointerException("Empty View")
                }
                mEmptyView?.visibility = VISIBLE
                if (mLoadingView != null)  mLoadingView?.visibility = GONE
                if (mErrorView != null) mErrorView?.visibility = GONE
                if (mContentView != null)  mContentView?.visibility = GONE
                if (mCustomView != null)  mCustomView?.visibility = GONE
            }
            ViewState.ERROR -> {
                if (mErrorView == null) {
                    throw NullPointerException("Error View")
                }
                mErrorView?.visibility = VISIBLE
                if (mLoadingView != null)  mLoadingView?.visibility = GONE
                if (mContentView != null)  mContentView?.visibility = GONE
                if (mEmptyView != null) mEmptyView?.visibility = GONE
                if (mCustomView != null)  mCustomView?.visibility = GONE
            }
            ViewState.CUSTOM -> {
                if (mCustomView == null) {
                    throw NullPointerException("Custom View")
                }
                 mCustomView?.visibility = VISIBLE
                if (mLoadingView != null)  mLoadingView?.visibility = GONE
                if (mContentView != null)  mContentView?.visibility = GONE
                if (mEmptyView != null) mEmptyView?.visibility = GONE
                if (mErrorView != null) mErrorView?.visibility = GONE
            }
            ViewState.CONTENT -> {
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw NullPointerException("Content View")
                }
                 mContentView?.visibility = VISIBLE
                if (mLoadingView != null)  mLoadingView?.visibility = GONE
                if (mErrorView != null) mErrorView?.visibility = GONE
                if (mEmptyView != null) mEmptyView?.visibility = GONE
                if (mCustomView != null)  mCustomView?.visibility = GONE
            }
        }
    }

    private fun isValidContentView(view: View?): Boolean {
        return if (mContentView != null && mContentView !== view) {
            false
        } else view !== mLoadingView && view !== mErrorView && view !== mEmptyView && view !== mCustomView
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.removeAllViews()
    }

    companion object {
        private const val UNKNOWN_VIEW = -1
        private const val CONTENT_VIEW = 0
        private const val ERROR_VIEW = 1
        private const val EMPTY_VIEW = 2
        private const val LOADING_VIEW = 3
        private const val CUSTOM_VIEW = 4
    }
}