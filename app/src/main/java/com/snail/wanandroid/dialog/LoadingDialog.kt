package com.snail.wanandroid.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.snail.wanandroid.R


class LoadingDialog : DialogFragment() {
    init {
        setStyle(STYLE_NO_TITLE, R.style.LoadingDialogStyle)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setCanceledOnTouchOutside(false)

        return inflater.inflate(R.layout.view_loading, container, false)
    }
}