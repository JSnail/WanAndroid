package com.snail.wanandroid.ui.home

import android.util.Log
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentSystemBinding

class SystemFragment : BaseFragment<FragmentSystemBinding>(R.layout.fragment_system) {
    override fun loadData() {
        vB.textDashboard.setOnClickListener {
            dialogViewLiveData.value = false
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "SystemFragment  onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "SystemFragment onResume")
    }

}