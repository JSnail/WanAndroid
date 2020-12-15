package com.snail.wanandroid.interfaceImpl

import androidx.transition.Transition


/**
 * @Author  Snail
 * @Date 12/14/20
 * @Description
 **/
class LoginTransitionInterfaceImpl
    constructor(private  val onTransitionEnd :() ->Unit)
    : Transition.TransitionListener {


    override fun onTransitionStart(transition: Transition) {

    }

    override fun onTransitionEnd(transition: Transition) {
        onTransitionEnd.invoke()
        transition.removeListener(this)
    }

    override fun onTransitionCancel(transition: Transition) {

    }

    override fun onTransitionPause(transition: Transition) {
        
    }

    override fun onTransitionResume(transition: Transition) {
        
    }

}