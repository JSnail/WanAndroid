package com.snail.wanandroid.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.viewmodel.LoadingViewLiveData
import org.koin.android.ext.android.inject

abstract class BaseFragment<T : ViewBinding> :  Fragment() {

    protected lateinit var vB: T
    protected val dialogViewLiveData: LoadingViewLiveData by inject()
    private val loadingDialog: LoadingDialog by inject()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vB = getViewBinding(inflater,container)
        startObserver()
        loadData()
        return vB.root
    }

    open fun goToActivity(cls: Class<*>) {
        startActivity(Intent(requireActivity(), cls))
    }

    open fun goToActivityForResult(cls: Class<*>, requestId: Int) {
        startActivityForResult(Intent(requireActivity(), cls), requestId)
    }
    open fun goToActivity(intent: Intent){
        startActivity(intent)
    }

    open fun goToActivity(intent: Intent,option: ActivityOptions){
        startActivity(intent,option.toBundle())
    }


    abstract fun getViewBinding(  inflater: LayoutInflater, container: ViewGroup?): T
    abstract fun loadData()
    open fun startObserver() {
        dialogViewLiveData.observe(this.viewLifecycleOwner, {
            if (it) loadingDialog.show(childFragmentManager, "loading") else loadingDialog.dismiss()
        })
    }

}