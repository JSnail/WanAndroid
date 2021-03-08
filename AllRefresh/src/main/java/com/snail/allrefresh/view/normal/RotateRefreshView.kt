package com.snail.allrefresh.view.normal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import com.snail.allrefresh.R
import com.snail.allrefresh.RefreshInterface

class RotateRefreshView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context!!, attrs, defStyleAttr
), RefreshInterface {

    override fun onPrepare() {}
    override fun onRelease() {}
    override fun onReleaseNoEnough(currentPercent: Float) {}
    override fun onComplete() {}
    override fun onPositionChange(currentPercent: Float) {}
    override fun setIsHeaderOrFooter(isHeader: Boolean) {}
    override fun onReset() {
        resetImageRotation()
    }

    private fun resetImageRotation() {}

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_rotate, null)
        addView(v)
    }
}