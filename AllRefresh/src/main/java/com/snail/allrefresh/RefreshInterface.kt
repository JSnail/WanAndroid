package com.snail.allrefresh

interface RefreshInterface {
    /**
     * 重置
     */
    fun onReset()


    /**
     * 下拉高度大于头部高度
     */
    fun onPrepare()


    /**
     * 放手后
     */
    fun onRelease()

    /**
     * 不足时放手后
     */
    fun onReleaseNoEnough(currentPercent: Float)

    /**
     * 刷新完成
     */
    fun onComplete()

    /**
     * 下拉高度与头部高度比例
     */
    fun onPositionChange(currentPercent: Float)


    /**
     * 是下拉还是上拉
     * @param isHeader boolean
     */
    fun setIsHeaderOrFooter(isHeader: Boolean)
}