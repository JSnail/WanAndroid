package com.snail.wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.snail.wanandroid.R
import com.snail.wanandroid.extensions.loadImage


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/
class HomeAdapter constructor(private val context: Context, private val data: MutableList<String>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adapter_home_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.image.loadImage(data[position])
    }


    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: AppCompatImageView = view as AppCompatImageView
    }

}