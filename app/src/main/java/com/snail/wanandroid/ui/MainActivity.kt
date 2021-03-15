package com.snail.wanandroid.ui

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityMainBinding
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.listener.OnScrollToTopListener
import com.snail.wanandroid.ui.collect.CollectActivity
import com.snail.wanandroid.ui.credits.RankActivity
import com.snail.wanandroid.ui.credits.RankDetailsActivity
import com.snail.wanandroid.ui.home.HomeFragment
import com.snail.wanandroid.ui.home.NavigationFragment
import com.snail.wanandroid.ui.home.ProjectFragment
import com.snail.wanandroid.ui.home.SystemFragment
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.ui.set.SystemSettingActivity
import com.snail.wanandroid.ui.share.ShareActivity
import com.snail.wanandroid.ui.todo.TodoActivity
import com.snail.wanandroid.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var fragments: Map<Int, Fragment>
    private var currentFragment: Fragment? = null
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var creditsView: TextView
    private lateinit var nicknameView: TextView
    private lateinit var rankView: TextView

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


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
        val title = when (id) {
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

    private fun initNavView() {
        val headView = vB.homeNavigationView.inflateHeaderView(R.layout.nav_header_main)
        vB.homeNavigationView.setNavigationItemSelectedListener(
            onDrawerNavigationItemSelectedListener
        )
        nicknameView = headView.findViewById(R.id.textHeaderNickname)
        rankView = headView.findViewById(R.id.textHeaderRank)

        nicknameView.onClick {
            if (!mainViewModel.isLogin) {
                goToActivity(LoginActivity::class.java)
            }
        }

        headView.findViewById<ImageView>(R.id.imageHeaderRank).onClick {
            goToActivity(RankActivity::class.java)
        }

        creditsView = vB.homeNavigationView.menu.findItem(R.id.credits).actionView as TextView
        creditsView.gravity = Gravity.CENTER_VERTICAL

    }

    override fun startObserver() {

        mainViewModel.userEntity.observe(this, {
            if (null != it) {
                mainViewModel.getUserRankInfo()
                nicknameView.text = it.username
            } else {
                nicknameView.text = getString(R.string.click_login)
                rankView.text = getString(R.string.user_rank, "--")
            }
            vB.homeNavigationView.menu.findItem(R.id.logout).isVisible = null != it
        })

        mainViewModel.userRankEntity.observe(this, {
            if (null == it) {
                creditsView.text = ""
                rankView.text = getString(R.string.user_rank, "--")
            } else {
                creditsView.text = it.coinCount.toString()
                rankView.text = getString(R.string.user_rank, it.rank.toString())
            }
        })
    }


    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener {
            if (!mainViewModel.isLogin) {
                goToActivity(LoginActivity::class.java)
                return@OnNavigationItemSelectedListener true
            }
            when (it.itemId) {
                R.id.credits -> goToActivity(RankDetailsActivity::class.java)
                R.id.collect -> goToActivity(CollectActivity::class.java)
                R.id.share -> goToActivity(ShareActivity::class.java)
                R.id.toDo -> goToActivity(TodoActivity::class.java)
                R.id.systemSet -> goToActivity(SystemSettingActivity::class.java)
                R.id.logout -> {
                    mainViewModel.logout()
                }
            }
            return@OnNavigationItemSelectedListener true
        }

    override fun onBackPressed() {
        if (vB.mainDrawerLayout.isOpen) {
            vB.mainDrawerLayout.close()
            return
        }
        super.onBackPressed()
    }
}