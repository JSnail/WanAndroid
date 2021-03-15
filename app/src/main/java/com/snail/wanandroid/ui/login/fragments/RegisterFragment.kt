package com.snail.wanandroid.ui.login.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentLoginBinding
import com.snail.wanandroid.databinding.FragmentRegisterBinding
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.extensions.showSnackBar
import com.snail.wanandroid.interfaceImpl.LoginTransitionInterfaceImpl
import com.snail.wanandroid.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRegisterBinding =
        FragmentRegisterBinding.inflate(inflater, container, false)

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun loadData() {

        val transition =
            TransitionInflater.from(activity).inflateTransition(R.transition.fabtransition)

        sharedElementEnterTransition = transition

        addShareElementEnterTransition(transition)


        vB.fabRegister.setOnClickListener { animateRevealClose() }

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    animateRevealClose()
                }
            })

        vB.btnRegisterGo.onClick {
            register()
        }
        vB.fabRegister.onClick {
            animateRevealClose()
        }
    }


    private fun addShareElementEnterTransition(transition: Transition) {
        transition.addListener(listener)
    }

    private val listener = LoginTransitionInterfaceImpl {
        vB.cvRegister.visibility = View.VISIBLE
        animateRevealShow()

    }


    private fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(
            vB.cvRegister, vB.cvRegister.width / 2, 0,
            (vB.fabRegister.width / 2).toFloat(), vB.cvRegister.height.toFloat()
        )
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                vB.cvRegister.visibility = View.VISIBLE
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    private fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(
            vB.cvRegister, vB.cvRegister.width / 2,
            0, vB.cvRegister.height.toFloat(), (vB.fabRegister.width / 2).toFloat()
        )
        mAnimator.duration = 300
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                vB.cvRegister.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
//                vB.fabRegister.setImageResource(R.drawable.ic_regiseter_close_24)
                Navigation.findNavController(vB.cvRegister).popBackStack()
            }

        })
        mAnimator.start()
    }

    override fun startObserver() {
        super.startObserver()
        registerViewModel.registerData.observe(this, {
            if (it) {
                vB.root.showSnackBar(R.string.register_success)
            }
        })
        registerViewModel.errorMessage.observe(this, {
            vB.root.showSnackBar(it)
        })
    }

    fun register() {
        val account = vB.editRegisterAccount.text.toString()
        if (account.isEmpty()) {
            vB.editRegisterAccountLayout.error = getString(R.string.error_account_empty)
            return
        }

        val pwd = vB.editRegisterPwd.text.toString()
        if (pwd.isEmpty()) {
            vB.editRegisterPwdLayout.error = getString(R.string.error_password_empty)
            return
        }
        val rePwd = vB.editRegisterRePwd.text.toString()
        if (rePwd.isEmpty()) {
            vB.editRegisterRePwdLayout.error = getString(R.string.error_password_empty)
            return
        }
        registerViewModel.startRegister(account, pwd)
    }
}