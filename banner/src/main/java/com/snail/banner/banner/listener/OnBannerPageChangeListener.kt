package com.snail.banner.banner.listener

interface OnBannerPageChangeListener {
    fun onPageSelected(position: Int)
    fun onPageScrollStateChanged(state: Int)
}