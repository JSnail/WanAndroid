package com.snail.wanandroid.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.viewmodel.LoadingViewLiveData
import org.koin.android.ext.android.inject

abstract class BaseFragment<T : ViewDataBinding> constructor(@LayoutRes private val layoutId: Int) :
    Fragment() {

    protected lateinit var vB: T
    protected val dialogViewLiveData: LoadingViewLiveData by inject()
    private val loadingDialog : LoadingDialog by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vB = DataBindingUtil.inflate(inflater, layoutId, container, false) as T
        vB.lifecycleOwner = this
        startObserver()
        loadData()
        return vB.root
    }


    abstract fun loadData()
    open fun startObserver() {
        dialogViewLiveData.observe(this.viewLifecycleOwner, {
            if (it) loadingDialog.show(childFragmentManager,"") else loadingDialog.dismiss()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vB.unbind()
    }
}