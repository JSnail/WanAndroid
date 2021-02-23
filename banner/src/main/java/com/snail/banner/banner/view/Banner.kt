package com.snail.banner.banner.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.base.project.banner.R
import com.base.project.banner.databinding.BannerLayoutBinding

class Banner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
//        BannerLayoutBinding.bind(
//            LayoutInflater.from(context).inflate(R.layout.banner_layout, this, false)
//        )
        BannerLayoutBinding.inflate(LayoutInflater.from(context))
    }

}