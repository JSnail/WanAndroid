package com.snail.allrefresh.view.normal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.snail.allrefresh.R
import com.snail.allrefresh.RefreshInterface
import com.snail.allrefresh.databinding.LayoutNormalRefreshBinding

class NormalRefreshView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    context, attrs, defStyleAttr
), RefreshInterface {
    private var viewBinding: LayoutNormalRefreshBinding = LayoutNormalRefreshBinding.inflate(LayoutInflater.from(context))
    private val rotateUp: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_up)
    private val rotateDown: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_down)
    private var rotated = false


    override fun onReset() {
        rotated = false
        viewBinding.ivArrow.clearAnimation()
        viewBinding.ivArrow.visibility = GONE
        viewBinding.progressBar.visibility = GONE
    }

    override fun onPrepare() {}
    override fun onComplete() {
        rotated = false
        viewBinding.ivArrow.clearAnimation()
        viewBinding.ivArrow.visibility = GONE
        viewBinding.progressBar.visibility = GONE
        viewBinding.tvRefresh.text = context.getString(R.string.refresh_complete)
    }

    override fun onRelease() {
        rotated = false
        viewBinding.ivArrow.clearAnimation()
        viewBinding.ivArrow.visibility = GONE
        viewBinding.progressBar.visibility = VISIBLE
        viewBinding.tvRefresh.text = context.getString(R.string.refresh_refreshing)
    }

    override fun onReleaseNoEnough(currentPercent: Float) {}
    override fun onPositionChange(currentPercent: Float) {
        viewBinding.ivArrow.visibility = VISIBLE
        viewBinding.progressBar.visibility = GONE
        if (currentPercent < 1) {
            if (rotated) {
                viewBinding.ivArrow.clearAnimation()
                viewBinding.ivArrow.startAnimation(rotateDown)
                rotated = false
            }
            viewBinding.tvRefresh.text = context.getString(R.string.refresh_pull)
        } else {
            viewBinding.tvRefresh.text = context.getString(R.string.refresh_release)
            if (!rotated) {
                viewBinding.ivArrow.clearAnimation()
                viewBinding.ivArrow.startAnimation(rotateUp)
                rotated = true
            }
        }
    }

    override fun setIsHeaderOrFooter(isHead: Boolean) {
        if (!isHead) {
            viewBinding.ivArrow.rotation = 180F
        }
    }
}