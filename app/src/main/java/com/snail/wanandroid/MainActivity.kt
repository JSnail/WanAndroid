package com.snail.wanandroid

import androidx.fragment.app.Fragment
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityMainBinding
import com.snail.wanandroid.ui.home.HomeFragment
import com.snail.wanandroid.ui.home.NavigationFragment
import com.snail.wanandroid.ui.home.ProjectFragment
import com.snail.wanandroid.ui.home.SystemFragment

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var fragments: Map<Int, Fragment>

    override fun loadData() {
        fragments = mapOf(
            R.id.main_home to createFragment(HomeFragment::class.java),
            R.id.main_system to createFragment(SystemFragment::class.java),
            R.id.main_navigation to createFragment(NavigationFragment::class.java),
            R.id.main_project to createFragment(ProjectFragment::class.java)
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
}