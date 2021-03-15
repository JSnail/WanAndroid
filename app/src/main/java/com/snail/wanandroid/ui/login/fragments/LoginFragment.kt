package com.snail.wanandroid.ui.login.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding
import com.snail.wanandroid.databinding.FragmentSystemBinding
import com.snail.wanandroid.extensions.onAfterTextChanged
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.extensions.showSnackBar
import com.snail.wanandroid.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(inflater, container, false)

    private val loginViewModel: LoginViewModel by viewModel()


    override fun loadData() {
        vB.editLoginAccount.onAfterTextChanged {
            vB.editLoginAccountLayout.error = null
        }
        vB.editLoginPwd.onAfterTextChanged {
            vB.editLoginPwdLayout.error = null
        }
        vB.btnLogin.onClick {
            login()
        }
        vB.fabToRegister.onClick {
            goToRegister(it)
        }
    }

   private fun login() {
        val account = vB.editLoginAccount.text.toString()
        if (account.isEmpty()) {
            vB.editLoginAccountLayout.error = getString(R.string.error_account_empty)
            return
        }

        val password = vB.editLoginPwd.text.toString()
        if (password.isEmpty()) {
            vB.editLoginPwdLayout.error = getString(R.string.error_password_empty)
            return
        }
        loginViewModel.login(account, password)
    }

    fun forgetPassword(view: View) {

    }

  private  fun goToRegister(view: View) {
        val extras =
            FragmentNavigatorExtras(vB.fabToRegister to getString(R.string.transitionName_login))
        val navController = Navigation.findNavController(view)
        navController.saveState()
        navController.navigate(
            R.id.action_loginFragment_to_registerFragment,
            null,
            null,
            extras
        )
    }

    override fun startObserver() {
        super.startObserver()
        loginViewModel.loginLiveData.observe(this, {
            if (it) {
                vB.root.showSnackBar(R.string.login_success)
                requireActivity().finish()
            }
        })

        loginViewModel.errorMessage.observe(this, {
            vB.root.showSnackBar(it)
        })
    }
}