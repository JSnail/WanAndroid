package com.snail.banner.banner.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.base.project.banner.R
import com.base.project.banner.databinding.BannerLayoutBinding
import com.snail.banner.banner.adapter.BannerAdapter
import com.snail.banner.banner.layoutmanager.BLayoutManager
import com.snail.banner.banner.layoutmanager.BannerLayoutManager
import com.snail.banner.banner.layoutmanager.CenterSnapHelper
import com.snail.banner.banner.listener.BannerLifeCycle
import com.snail.banner.banner.listener.OnBannerLifeListener

class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), OnBannerLifeListener {

    private var isStarted = false
    private val indicatorViews = mutableListOf<View>()

    private var viewBinding: BannerLayoutBinding = BannerLayoutBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.banner_layout, this, true)
    )


    fun startLoop(imageUrls: MutableList<String>, lifecycleOwner: LifecycleOwner) {
        if (!isStarted) {
            isStarted = true
            viewBinding.bannerRecycler.apply {
                this.layoutManager = BannerLayoutManager()
                CenterSnapHelper().attachToRecyclerView(this)
                this.adapter = BannerAdapter(context, imageUrls)
            }
            initIndicator(imageUrls.size)
//            val bannerLifeCycle = BannerLifeCycle(this, imageUrls.size )
//            lifecycleOwner.lifecycle.addObserver(bannerLifeCycle)
        }
    }

    private fun initIndicator(size: Int) {
        for (i in 0..size) {
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
        viewBinding.bannerRecycler.smoothScrollToPosition(position)
        Log.i("TAG","onNext  position  == $position")
        changeIndicator(position)
    }

    private fun changeIndicator(position: Int) {
        indicatorViews.forEachIndexed { index, view ->
            view.isSelected = index == position
        }
    }

}