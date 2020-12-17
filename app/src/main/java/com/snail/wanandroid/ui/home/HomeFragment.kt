package com.snail.wanandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.snail.wanandroid.R
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.ui.login.LoginActivity
import com.snail.wanandroid.widget.LoadingView

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val loadingView = root.findViewById<LoadingView>(R.id.loadingView)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        var i =0
        textView.setOnClickListener {
//            startActivity(Intent(activity,LoginActivity::class.java))
            when {
                i % 2 ==0 -> {
                    loadingView.loadSuccess(LoadingView.STATUS.SUCCESS)
                }
                i % 3 == 0 -> {
                    loadingView.loadSuccess(LoadingView.STATUS.LOADING)
                }
                else -> {
                    loadingView.loadSuccess(LoadingView.STATUS.FAIL)
                }
            }
            i++
        }

        return root
    }
}