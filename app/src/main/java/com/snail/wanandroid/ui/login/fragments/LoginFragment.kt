package com.snail.wanandroid.ui.login.fragments

import android.view.View
import androidx.navigation.Navigation
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun loadData() {
        vB.fabToRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    fun goToRegister(view:View){

    }
}