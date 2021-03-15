package com.snail.wanandroid.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentProjectBinding
import com.snail.wanandroid.databinding.FragmentSystemBinding

class SystemFragment : BaseFragment<FragmentSystemBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSystemBinding =
        FragmentSystemBinding.inflate(inflater, container, false)

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