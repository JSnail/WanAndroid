package com.snail.wanandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snail.wanandroid.R
import com.snail.wanandroid.databinding.AdapterHomeItemLayoutBinding


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
            AdapterHomeItemLayoutBinding.bind(
                LayoutInflater.from(context)
                    .inflate(R.layout.adapter_home_item_layout, parent, false)
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


    inner class HomeViewHolder(view: AdapterHomeItemLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {
        val image: View = view.root
        val text: TextView = view.text
    }

}