package com.snail.banner.banner.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.base.project.banner.R
import com.base.project.banner.databinding.BannerItemLayoutBinding


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/
class BannerAdapter constructor(private val context: Context, private val data: MutableList<String>) :
    RecyclerView.Adapter<BannerAdapter.HomeViewHolder>() {

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            BannerItemLayoutBinding.bind(
                LayoutInflater.from(context)
                    .inflate(R.layout.banner_item_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
//        holder.image.loadImage(data[position])
        when(position % 5){
            0 ->  holder.image.setBackgroundColor(context.resources.getColor(android.R.color.holo_green_light))
            1 ->  holder.image.setBackgroundColor(context.resources.getColor(android.R.color.holo_orange_light))
            2 ->  holder.image.setBackgroundColor(context.resources.getColor(android.R.color.holo_blue_bright))
            3 ->  holder.image.setBackgroundColor(context.resources.getColor(android.R.color.holo_purple))
            4 ->  holder.image.setBackgroundColor(context.resources.getColor(android.R.color.holo_red_light))
        }
        holder.text.text = "Item $position"
    }


    inner class HomeViewHolder(view: BannerItemLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {
        val image: View = view.root
        val text: TextView = view.text
    }

}