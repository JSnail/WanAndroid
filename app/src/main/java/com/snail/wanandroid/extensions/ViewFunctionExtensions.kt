package com.snail.wanandroid.extensions

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText


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
