package com.snail.wanandroid

import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityMainBinding
import com.snail.wanandroid.listener.OnScrollToTopListener
import com.snail.wanandroid.ui.home.HomeFragment
import com.snail.wanandroid.ui.home.NavigationFragment
import com.snail.wanandroid.ui.home.ProjectFragment
import com.snail.wanandroid.ui.home.SystemFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var fragments: Map<Int, Fragment>
    private var currentFragment: Fragment? = null

    override fun loadData() {
        fragments = mapOf(
            R.id.main_home to createFragment(HomeFragment::class.java),
            R.id.main_system to createFragment(SystemFragment::class.java),
            R.id.main_navigation to createFragment(NavigationFragment::class.java),
            R.id.main_project to createFragment(ProjectFragment::class.java)
        )

        vB.navView.apply {
            setOnNavigationItemSelectedListener {
                showFragment(it.itemId)
                return@setOnNavigationItemSelectedListener true
            }

            setOnNavigationItemReselectedListener {
                currentFragment?.let { fragment ->
                    if (fragment is OnScrollToTopListener) fragment.onScrollToTop()
                }
            }
        }
        showFragment(R.id.main_home)
        vB.mainToolbar.apply {
            this.title = "aaaa"
            setSupportActionBar(this)
        }
        ActionBarDrawerToggle(
            this,
            vB.mainDrawerLayout,
            vB.mainToolbar,
            R.string.app_name,
            R.string.title_home
        )
            .apply {
                vB.mainDrawerLayout.addDrawerListener(this)
                this.syncState()
            }
        AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_gallery), vB.mainDrawerLayout)
    }

    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { clazz == it.javaClass }
        if (null == fragment) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment()
                SystemFragment::class.java -> SystemFragment()
                NavigationFragment::class.java -> NavigationFragment()
                ProjectFragment::class.java -> ProjectFragment()
                else -> throw IllegalArgumentException("argument ${clazz.simpleName} is illegal")
            }
        }
        return fragment
    }

    private fun showFragment(@IdRes id: Int) {
        currentFragment = supportFragmentManager.fragments.find { it.isVisible }
        val targetFragment = fragments.entries.find { it.key == id }?.value
        supportFragmentManager.beginTransaction()
            .apply {
                currentFragment?.let {
                    if (it.isVisible) {
                        this.hide(it)
                    }
                }
                targetFragment?.let {
                    if (it.isAdded) this.show(it) else this.add(R.id.fragmentContainer, it)
                }
            }.commit()
    }
}