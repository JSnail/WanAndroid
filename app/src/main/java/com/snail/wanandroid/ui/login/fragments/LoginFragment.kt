package com.snail.wanandroid.ui.login.fragments

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding
import com.snail.wanandroid.db.AppDataBase
import com.snail.wanandroid.dialog.LoadingDialog
import com.snail.wanandroid.extensions.onClick
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

     fun login(view:View){
         Log.d("TAG","点击登录")
         val account = vB.editLoginAccount.text.toString()
         if (account.isEmpty()){
             vB.editLoginAccount.error = getString(R.string.error_account_empty)
             return
         }

         val password = vB.editLoginPwd.text.toString()
         if (password.isEmpty()){

             return
         }
         loginViewModel.login(account,password)
    }

    fun forgetPassword(view:View){

    }

    fun goToRegister(view:View){

    }

    override fun startObserver() {
        super.startObserver()
        loginViewModel.loginLiveData.observe(this,{
            if (it)Log.i("TAG","登录成功") else Log.i("TAG","登录失败")
        })
    }
}