package com.snail.wanandroid.entity

import androidx.recyclerview.widget.DiffUtil

open class BaseHomeAllEntity {
    var itemType = banner

    companion object {
        const val banner = 0
        const val topArticle = 1
        const val commonArticle = 2
    }
}

object HomeDiffCallback : DiffUtil.ItemCallback<BaseHomeAllEntity>() {

    override fun areItemsTheSame(oldItem: BaseHomeAllEntity, newItem: BaseHomeAllEntity): Boolean {
        return when {
            oldItem is ArticleTopEntity && newItem is ArticleTopEntity -> oldItem.id == newItem.id
            oldItem is ArticleListBean && newItem is ArticleListBean -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: BaseHomeAllEntity, newItem: BaseHomeAllEntity) = when {
        oldItem is ArticleTopEntity && newItem is ArticleTopEntity -> oldItem.id == newItem.id
        oldItem is ArticleListBean && newItem is ArticleListBean -> oldItem.id == newItem.id
        else -> false
    }
}
