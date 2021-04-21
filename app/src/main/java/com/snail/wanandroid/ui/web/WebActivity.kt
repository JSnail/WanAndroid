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
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityWebBinding
import com.snail.wanandroid.db.UserDataManager
import com.snail.wanandroid.entity.WebDataEntity
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.ui.home.HomeFragment
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.viewmodel.WebViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.HashMap


class WebActivity : BaseActivity<ActivityWebBinding>() {
    private lateinit var urls: ArrayList<WebDataEntity>
    private var title = ""
    private lateinit var currentBean: WebDataEntity

    private val viewModel : WebViewModel by viewModel ()
    private val changeId = ArrayList<HashMap<Int,Boolean>>()

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

    fun setTitle(title: String) {
        vB.webToolbar.title = title
    }

    override fun loadData() {
        urls = intent.getParcelableArrayListExtra(URLS) ?: ArrayList()
        title = intent.getStringExtra(TITLE) ?: ""
        vB.webViewPager.adapter = ViewPagerAdapter(this)
        vB.webViewPager.offscreenPageLimit = 7
        vB.webViewPager.currentItem = HomeFragment.currentPosition
        vB.webViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                HomeFragment.currentPosition = position
                currentBean = urls[position]
                val fragment =
                    supportFragmentManager.findFragmentByTag("f${HomeFragment.currentPosition}") as WebFragment
                setTitle(fragment.title)
                val collected = urls[position].isCollect
                vB.fab.setImageResource(
                    if (collected) {
                        R.drawable.ic_web_colleted
                    } else {
                        R.drawable.ic_web_uncolleted
                    }
                )
            }
        })
        vB.webToolbar.title = title

        currentBean = urls[HomeFragment.currentPosition]
        vB.fab.onClick {
            if (UserDataManager.instance.isLogged) {
                if (currentBean.isCollect){
                    viewModel.unCollect(currentBean.id)
                }else{
                    viewModel.collect(currentBean.id)
                }

            }else{
                goToActivity(LoginActivity::class.java)
            }
        }
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
                args.putString(WebFragment.URL, urls[position].url)
                arguments = args
            }

        }
    }

    companion object {
        const val URLS = "web_urls"
        const val TITLE = "web_title"
    }
}