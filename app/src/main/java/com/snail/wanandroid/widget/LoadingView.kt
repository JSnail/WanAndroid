package com.snail.wanandroid.widget

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.snail.wanandroid.R

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var viewStatus = STATUS.LOADING
    private var circleAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)
    private var circleValueProgress = 0F
    private var successValueProgress = 0F
    private var failValueLeftProgress = 0F
    private var failValueRightProgress = 0F


    /**
     *圆弧所能绘制的最大值
     **/
    private val maxArc = 300F

    /**
     *绘制的弧度
     **/
    private var currentSweep = 0F

    /**
     *开始弧度
     **/
    private var startArc = 0F
    private var radius = 0F
    private var lineWidth = context.resources.getDimension(R.dimen.x4)

    private val successPath = Path()
    private val failLeftPath = Path()
    private val failRightPath = Path()
    private val circlePath = Path()
    private val cachePath = Path()

    private val pathMeasure = PathMeasure()

    private var onLoadingListener:OnLoadingListener?=null


    private val circlePaint = Paint().apply {
        color = Color.parseColor("#FF018786")
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = lineWidth
    }

    private val successPaint = Paint().apply {
        color = Color.parseColor("#32CD32")
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = lineWidth
    }

    private val failPaint = Paint().apply {
        color = Color.parseColor("#FF0000")
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = lineWidth
    }

    init {
        circleAnimator.duration = 1000L
        circleAnimator.addUpdateListener {
            circleValueProgress = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)
        val model = MeasureSpec.getMode(widthMeasureSpec)
        val width = if (model == MeasureSpec.EXACTLY) {
            size
        } else {
            context.resources.getDimensionPixelOffset(R.dimen.x100)
        }
        radius = width / 2F
        setMeasuredDimension(width, width)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(radius, radius)
        when (viewStatus) {
            STATUS.LOADING -> drawArc(canvas)
            STATUS.SUCCESS -> drawSuccess(canvas)
            STATUS.FAIL -> drawFail(canvas)
        }
    }

    private var isFlag = false
    private var currentAngle = 0F
    private fun drawArc(canvas: Canvas) {

        if (startArc == 0F || !isFlag) {
            currentSweep += 6F
        }
        if (currentSweep == maxArc) {
            isFlag = true
        }
        if (isFlag) {
            startArc += 6F
            currentSweep -= 6F
            if (currentSweep == 30F) {
                isFlag = false
            }
        }
        currentAngle += 4F
        canvas.rotate(currentAngle, 0F, 0F)
        canvas.drawArc(
            RectF(-radius + lineWidth, -radius + lineWidth, radius - lineWidth, radius - lineWidth),
            startArc,
            currentSweep,
            false,
            circlePaint
        )
        invalidate()
    }

    private fun drawSuccess(canvas: Canvas) {
        circlePath.addCircle(0F, 0F, radius - lineWidth, Path.Direction.CW)
        pathMeasure.setPath(circlePath, false)
        pathMeasure.getSegment(0F, circleValueProgress * pathMeasure.length, cachePath, true)
        canvas.drawPath(cachePath, successPaint)

        if (circleValueProgress == 1F) {
            successPath.moveTo(-radius * 3 / 5, -radius / 6)
            successPath.lineTo(-radius / 6, radius * 0.35F)
            successPath.lineTo(radius * 3 / 5, -radius * 2 / 5)
            pathMeasure.nextContour()
            pathMeasure.setPath(successPath, false)
            pathMeasure.getSegment(0F, successValueProgress * pathMeasure.length, cachePath, true)
            canvas.drawPath(cachePath, successPaint)
        }

    }

    private fun drawFail(canvas: Canvas) {
        circlePath.addCircle(0F, 0F, radius - lineWidth, Path.Direction.CW)
        pathMeasure.setPath(circlePath, false)
        pathMeasure.getSegment(0F, circleValueProgress * pathMeasure.length, cachePath, true)
        canvas.drawPath(cachePath, failPaint)

        if (circleValueProgress == 1F) {
            failLeftPath.moveTo(-radius / 2, -radius / 2)
            failLeftPath.lineTo(radius / 2, radius / 2)
            pathMeasure.nextContour()
            pathMeasure.setPath(failLeftPath, false)
            pathMeasure.getSegment(0F, failValueLeftProgress * pathMeasure.length, cachePath, true)
            canvas.drawPath(cachePath, failPaint)
        }

        if (failValueLeftProgress ==1F){
            failRightPath.moveTo(radius / 2, -radius / 2)
            failRightPath.lineTo(-radius / 2, radius / 2)
            pathMeasure.nextContour()
            pathMeasure.setPath(failRightPath, false)
            pathMeasure.getSegment(0F, failValueRightProgress * pathMeasure.length, cachePath, true)
            canvas.drawPath(cachePath, failPaint)
        }

    }

    private fun startSuccessAnimation() {
        val success = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                successValueProgress = it.animatedValue as Float
                if (successValueProgress == 1F){
                    onLoadingListener?.onComplete()
                }
                invalidate()
            }
        }
        AnimatorSet().apply {
            this.play(success).after(circleAnimator)
            this.duration = 500
        }.start()
    }


    private fun startFailAnimation() {
        val failLeft = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                failValueLeftProgress = it.animatedValue as Float
                invalidate()
            }
        }
        val failRight = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                failValueRightProgress = it.animatedValue as Float
                if (failValueRightProgress == 1F){
                    onLoadingListener?.onComplete()
                }
                invalidate()
            }
        }
        AnimatorSet().apply {
            this.playSequentially(circleAnimator,failLeft,failRight)
            this.duration = 300
        }.start()

    }

    fun loadSuccess(status: STATUS) {
        reset()
        viewStatus = status
        when (status) {
            STATUS.LOADING -> invalidate()
            STATUS.SUCCESS -> startSuccessAnimation()
            STATUS.FAIL -> startFailAnimation()
        }
    }

    private fun reset() {
        circleValueProgress = 0F
        successValueProgress = 0F
        failValueRightProgress = 0F
        failValueRightProgress = 0F
        cachePath.reset()
        successPath.reset()
        failLeftPath.reset()
        failRightPath.reset()
        circlePath.reset()
    }

    fun addOnLoadingListener(onComplete:() ->Unit){
        this.onLoadingListener = object :OnLoadingListener{
            override fun onComplete() {
                onComplete.invoke()
            }
        }
    }

    enum class STATUS {
        LOADING, SUCCESS, FAIL
    }

    interface OnLoadingListener{
        fun onComplete()
    }
}
