package com.snail.wanandroid.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.snail.wanandroid.R

object LoadingDialog {
    private var dialog: AlertDialog? = null


    fun showLoadingDialog(context: Context) {
        dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.view_loading)
            .show()
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun dismiss() {
        dialog?.isShowing?.let {
            if (it) dialog?.dismiss()
        }
    }
}