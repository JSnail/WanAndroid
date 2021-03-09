package com.snail.wanandroid.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder constructor(view: View):RecyclerView.ViewHolder(view) {

    fun getItemView() :View = this.itemView
}