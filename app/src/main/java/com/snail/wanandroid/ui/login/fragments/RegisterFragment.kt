package com.snail.wanandroid.ui.login.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseFragment
import com.snail.wanandroid.databinding.FragmentRegisterBinding
import com.snail.wanandroid.interfaceImpl.LoginTransitionInterfaceImpl
import com.snail.wanandroid.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Retrofit

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(R.layout.fragment_register) {

    private val registerRepository :RegisterViewModel by viewModel()

    override fun loadData() {

      val  transition = TransitionInflater.from(activity).inflateTransition(R.transition.fabtransition)

        sharedElementEnterTransition = transition

        addShareElementEnterTransition(transition)


        vB.fabRegister.setOnClickListener { animateRevealClose() }

        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    animateRevealClose()
                }
            })
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
                vB.fabRegister.setImageResource(R.drawable.ic_regiseter_close_24)
                Navigation.findNavController(vB.cvRegister).popBackStack()
            }

        })
        mAnimator.start()
    }

    fun register(view:View){
        val name = vB.editRegisterAccount.text.toString()
    }
}