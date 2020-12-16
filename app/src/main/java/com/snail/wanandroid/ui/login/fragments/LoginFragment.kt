package com.snail.wanandroid.ui.login.fragments

import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val loginViewModel : LoginViewModel by  viewModel()


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