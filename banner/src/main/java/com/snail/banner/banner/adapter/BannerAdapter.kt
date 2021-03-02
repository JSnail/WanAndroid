package com.snail.banner.banner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.base.project.banner.databinding.BannerItemLayoutBinding
import com.snail.banner.banner.listener.OnImageLoadListener
import com.snail.banner.banner.listener.OnItemClickListener


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/
class BannerAdapter constructor(
    private val context: Context,
    private val data: MutableList<String>
) :
    RecyclerView.Adapter<BannerAdapter.HomeViewHolder>() {

    var onImageLoadListener: OnImageLoadListener? = null
    var onItemClickListener: OnItemClickListener? = null


    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(BannerItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        onImageLoadListener?.onLoadImage(holder.image, data[position])
        holder.image.setOnClickListener { onItemClickListener?.onItemClick(holder.image, position) }
    }


    inner class HomeViewHolder(view: BannerItemLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {
        val image: ImageView = view.image
    }

}