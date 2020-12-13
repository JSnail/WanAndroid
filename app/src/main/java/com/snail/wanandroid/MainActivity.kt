package com.snail.wanandroid

import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.snail.wanandroid.base.BaseActivity
import com.snail.wanandroid.databinding.ActivityMainBinding
import com.snail.wanandroid.databinding.ActivityMainBindingImpl

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {


    override fun loadData() {
        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        findViewById<BottomNavigationView>(R.id.navView)
            .setupWithNavController(navController)
    }
}