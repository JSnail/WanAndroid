package com.snail.wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseViewHolder
import com.snail.wanandroid.databinding.ItemHomeBannerLayoutBinding
import com.snail.wanandroid.databinding.ItemHomeCommentLayoutBinding
import com.snail.wanandroid.databinding.ItemHomeTopLayoutBinding
import com.snail.wanandroid.entity.BaseHomeAllEntity
import com.snail.wanandroid.entity.HomeBannerEntity
import com.snail.wanandroid.extensions.loadImage


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/
class HomeAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data = mutableListOf<BaseHomeAllEntity>()

    fun setData(data: MutableList<BaseHomeAllEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return data[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BaseHomeAllEntity.banner -> HomeBannerViewHolder(
                ItemHomeBannerLayoutBinding.bind(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_home_banner_layout, parent, false)
                )
            )
            BaseHomeAllEntity.topArticle -> HomeTopViewHolder(
                ItemHomeTopLayoutBinding.bind(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_home_top_layout, parent, false)
                )
            )
            BaseHomeAllEntity.commonArticle -> HomeCommentViewHolder(
                ItemHomeCommentLayoutBinding.bind(
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_home_comment_layout, parent, false)
                )
            )
            else -> throw IllegalArgumentException("")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            BaseHomeAllEntity.banner -> {
                val bannerDatas = data[position] as HomeBannerEntity
                (holder as HomeBannerViewHolder).setData(bannerDatas)
            }
            BaseHomeAllEntity.topArticle -> {
            }
            BaseHomeAllEntity.commonArticle -> {
            }
        }
    }


    /**
     *banner
     **/
    inner class HomeBannerViewHolder(private  val view: ItemHomeBannerLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun setData(bean: HomeBannerEntity) {
            val urls = mutableListOf<String>()
            bean.bannerDatas?.forEach {
                urls.add(it.imagePath)
            }
            view.root.BannerBuilder()
                .addLifecycleOwner((context as FragmentActivity))
                .setData(urls)
                .setOnImageLoadListener { imageView, s ->
                    imageView.loadImage(s)
                }
                .setOnItemClickListener { _, _ ->

                }
                .build()
        }

    }

    /**
     *置顶文章
     **/
    inner class HomeTopViewHolder(view: ItemHomeTopLayoutBinding) : BaseViewHolder(view.root) {

    }

    /**
     *普通文章
     **/
    inner class HomeCommentViewHolder(view: ItemHomeCommentLayoutBinding) :
        BaseViewHolder(view.root) {

    }
}