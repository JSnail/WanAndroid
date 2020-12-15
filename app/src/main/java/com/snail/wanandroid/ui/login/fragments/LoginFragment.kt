package com.snail.wanandroid.ui.login.fragments

import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    override fun loadData() {
        vB.fabToRegister.setOnClickListener {
            val extras =
                FragmentNavigatorExtras(vB.fabToRegister to getString(R.string.transitionName_login))
            val navController = Navigation.findNavController(it)
            navController.saveState()
            navController.navigate(
                R.id.action_loginFragment_to_registerFragment,
                null,
                null,
                extras
            )

        }
    }

}