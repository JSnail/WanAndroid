package com.snail.wanandroid.ui.web

import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityWebBinding
import com.snail.wanandroid.ui.home.HomeFragment
import java.util.*


class WebActivity : BaseActivity<ActivityWebBinding>() {
    private lateinit var urls: ArrayList<String>
    private var title = ""

    override fun getViewBinding(): ActivityWebBinding {
        return ActivityWebBinding.inflate(layoutInflater)
    }

    override fun configure() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300
        }
        window.sharedElementExitTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300
        }
    }

    override fun loadData() {
        urls = intent.getStringArrayListExtra(URLS) ?: ArrayList()
        title = intent.getStringExtra(TITLE) ?: ""
        vB.webViewPager.adapter = ViewPagerAdapter(this)
        vB.webViewPager.offscreenPageLimit = 7
        vB.webViewPager.currentItem = HomeFragment.currentPosition
        vB.webViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                HomeFragment.currentPosition = position
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val fragment =
            supportFragmentManager.findFragmentByTag("f${HomeFragment.currentPosition}") as WebFragment
        return fragment.onKeyDown(keyCode, event, callback = {
            super.onKeyDown(keyCode, event)
        })
    }

    private inner class ViewPagerAdapter(activity: FragmentActivity) :

        FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = urls.size

        override fun createFragment(position: Int): Fragment {
            return WebFragment().apply {
                val args = Bundle()
                args.putString(WebFragment.URL, urls[position])
                arguments = args
            }

        }
    }

    companion object {
        const val URLS = "web_urls"
        const val TITLE = "web_title"
    }
}