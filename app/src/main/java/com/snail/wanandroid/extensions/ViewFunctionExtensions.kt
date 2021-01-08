package com.snail.wanandroid.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.snackbar.Snackbar


/**
 * @Author  Snail
 * @Date 1/5/21
 * @Description
 **/

fun AppCompatEditText.onAfterTextChanged(change:()->Unit){
    this.addTextChangedListener(object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            change.invoke()
        }
    })
}

fun View.showSnackBar(@StringRes message:Int){
    Snackbar.make(this,this.context.getString(message),Snackbar.LENGTH_SHORT).show()
}

fun View.showSnackBar( message:String){
    Snackbar.make(this,message,Snackbar.LENGTH_SHORT).show()
}
