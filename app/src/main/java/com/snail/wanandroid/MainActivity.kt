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

        initNavView()

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
            setSupportActionBar(this)
        }
        ActionBarDrawerToggle(
            this,
            vB.mainDrawerLayout,
            vB.mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
            .apply {
                vB.mainDrawerLayout.addDrawerListener(this)
                this.syncState()
            }
        AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery
            ), vB.mainDrawerLayout
        )
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
        showToolbarTitle(id)
    }

    private fun showToolbarTitle(@IdRes id: Int) {
      val  title =  when (id) {
            R.id.main_home -> R.string.title_home
            R.id.main_system -> R.string.title_navigation
            R.id.main_navigation -> R.string.title_system
            R.id.main_project -> R.string.title_project
            else -> {
                throw  IllegalArgumentException()
            }
        }
        vB.mainToolbar.title = getString(title)
    }

    private fun initNavView(){
        val headView = vB.homeNavigationView.inflateHeaderView(R.layout.nav_header_main)

    }
}