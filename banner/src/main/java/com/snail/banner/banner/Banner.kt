package com.snail.banner.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.base.project.banner.R
import com.base.project.banner.databinding.BannerLayoutBinding
import com.snail.banner.banner.adapter.BannerAdapter
import com.snail.banner.banner.layoutmanager.BannerLayoutManager
import com.snail.banner.banner.listener.BannerLifeCycle
import com.snail.banner.banner.listener.OnBannerLifeListener
import com.snail.banner.banner.listener.OnImageLoadListener
import com.snail.banner.banner.listener.OnItemClickListener
import com.snail.banner.banner.snap.BannerSnapHelper

class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), OnBannerLifeListener {

    private var isStarted = false
    private var currentPosition = -1
    private var isTouch = false
    private val indicatorViews = mutableListOf<View>()
    private var bannerLifeCycle: BannerLifeCycle? = null

    private var viewBinding: BannerLayoutBinding = BannerLayoutBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.banner_layout, this, true)
    )


    private fun initIndicator(size: Int) {
        for (i in 0 until size) {
            val view = View(context)
            view.isSelected = i == 0
            view.setBackgroundResource(R.drawable.drawable_indicator)
            val layoutParams =
                MarginLayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.width = resources.getDimensionPixelOffset(R.dimen.indicatorWidth)
            layoutParams.height = resources.getDimensionPixelOffset(R.dimen.indicatorHeight)
            layoutParams.rightMargin = resources.getDimensionPixelOffset(R.dimen.indicatorMargin)
            view.layoutParams = layoutParams
            viewBinding.bannerIndicatorLayout.addView(view)
            indicatorViews.add(view)
        }
    }

    override fun onNext(position: Int) {
        this.currentPosition = position + 1
        viewBinding.bannerRecycler.smoothScrollToPosition(currentPosition)
        Log.i("TAG", "onNext  position  == $position")
        changeIndicator(currentPosition)
    }

    private fun changeIndicator(position: Int) {
        var mPosition = position
        if (position == indicatorViews.size) {
            mPosition = 0
        }
        indicatorViews.forEachIndexed { index, view ->
            view.isSelected = index == mPosition
        }
    }


    inner class BannerBuilder {
        private var isAutoPlaying = BannerConfig.BANNER_IS_AUTO
        private var intervalTime = BannerConfig.BANNER_LOOP_TIME
        private var data = mutableListOf<String>()
        private var lifecycleOwner: LifecycleOwner? = null
        private var onImageLoadListener: OnImageLoadListener? = null
        private var onItemClickListener: OnItemClickListener? = null


        fun setData(data: MutableList<String>): BannerBuilder {
            this.data = data
            return this
        }

        fun addLifecycleOwner(lifecycleOwner: LifecycleOwner?): BannerBuilder {
            this.lifecycleOwner = lifecycleOwner
            return this
        }

        fun isAutoPlaying(isAutoPlaying: Boolean): BannerBuilder {
            this.isAutoPlaying = isAutoPlaying
            return this
        }

        fun setInterval(intervalTime: Long): BannerBuilder {
            this.intervalTime = intervalTime
            return this
        }

        fun setOnImageLoadListener(loadImage: (ImageView, String) -> Unit): BannerBuilder {
            this.onImageLoadListener = object : OnImageLoadListener {
                override fun onLoadImage(view: ImageView, url: String) {
                    loadImage.invoke(view, url)
                }

            }
            return this
        }

        fun setOnItemClickListener(listener: (ImageView, Int) -> Unit): BannerBuilder {
            this.onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(view: ImageView, position: Int) {
                    listener.invoke(view, position)
                }
            }
            return this
        }

        fun build() {
            if (!isStarted) {
                isStarted = true
                viewBinding.bannerRecycler.apply {
                    this.layoutManager = BannerLayoutManager()
                    BannerSnapHelper().attachToRecyclerView(this)
                    val adapter = BannerAdapter(context, data)
                    adapter.onImageLoadListener = onImageLoadListener
                    adapter.onItemClickListener = onItemClickListener
                    this.adapter = adapter
                }
                initIndicator(data.size)

                if (isAutoPlaying) {
                    if (null == lifecycleOwner) {
                        throw NullPointerException("lifecycleOwner  can not be null ")
                    }
                    bannerLifeCycle = BannerLifeCycle(this@Banner, data.size, intervalTime)
                    lifecycleOwner?.lifecycle?.addObserver(bannerLifeCycle!!)
                }

                viewBinding.bannerRecycler.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && isTouch) {
                            isTouch = false
                            bannerLifeCycle?.onResume()
                            val position = (recyclerView.layoutManager as BannerLayoutManager).getCurrentPosition()
                            changeIndicator(position)
                        }
                    }
                })

            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                bannerLifeCycle?.pause()
                isTouch = true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}