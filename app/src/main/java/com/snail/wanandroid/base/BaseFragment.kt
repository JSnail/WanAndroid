package com.snail.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment< T : ViewDataBinding> constructor(@LayoutRes private val layoutId: Int) : Fragment() {

    protected lateinit var vB: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vB = DataBindingUtil.inflate(inflater, layoutId, container, false) as T
        vB.lifecycleOwner = this
        loadData()
        return vB.root
    }


    abstract fun loadData()
    override fun onDestroyView() {
        super.onDestroyView()
        vB.unbind()
    }
}