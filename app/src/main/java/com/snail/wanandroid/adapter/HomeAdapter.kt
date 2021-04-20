package com.snail.wanandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snail.banner.banner.Banner
import com.snail.wanandroid.R
import com.snail.wanandroid.base.BaseViewHolder
import com.snail.wanandroid.databinding.ItemHomeBannerLayoutBinding
import com.snail.wanandroid.databinding.ItemHomeCommentLayoutBinding
import com.snail.wanandroid.databinding.ItemHomeTopLayoutBinding
import com.snail.wanandroid.entity.*
import com.snail.wanandroid.extensions.loadImage
import com.snail.wanandroid.extensions.onClick
import com.snail.wanandroid.widget.home.HomeCollectSwipeActionDrawable
import com.snail.wanandroid.widget.home.ReboundingSwipeActionCallback
import kotlin.math.abs


/**
 * @Author  Snail
 * @Date 2/11/21
 * @Description
 **/
class HomeAdapter constructor(
    private val context: Context,
    private val listener: HomeAdapterListener
) :
    ListAdapter<BaseHomeAllEntity, RecyclerView.ViewHolder>(HomeDiffCallback) {

    private var data = mutableListOf<BaseHomeAllEntity>()
    private var banner: Banner? = null
    private val starredCornerSize = context.resources.getDimension(R.dimen.x48)

    fun setData(data: MutableList<BaseHomeAllEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<BaseHomeAllEntity>) {
        this.data.addAll(data)
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
                val bean = data[position] as ArticleTopEntity
                (holder as HomeTopViewHolder).setData(bean,position)
            }
            BaseHomeAllEntity.commonArticle -> {
                val bean = data[position] as ArticleListBean
                (holder as HomeCommentViewHolder).setData(bean,position)
            }
        }
    }


    /**
     *banner
     **/
    inner class HomeBannerViewHolder(private val view: ItemHomeBannerLayoutBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun setData(bean: HomeBannerEntity) {
            banner = view.root
            val urls = mutableListOf<String>()
            bean.bannerDatas?.forEach {
                urls.add(it.imagePath)
            }

            banner?.BannerBuilder()?.addLifecycleOwner((context as FragmentActivity))?.setData(urls)
                ?.setOnImageLoadListener { imageView, s ->
                    imageView.loadImage(s)
                }?.setOnItemClickListener { _, _ ->
                }?.build()
        }

    }

    /**
     *置顶文章
     **/
    inner class HomeTopViewHolder(private val view: ItemHomeTopLayoutBinding) :
        BaseViewHolder(view.root), ReboundingSwipeActionCallback.ReboundAbleViewHolder {
        private lateinit var bean: ArticleTopEntity

        init {
            view.run {
                this.root.background = HomeCollectSwipeActionDrawable(context)
            }
        }

        fun setData(bean: ArticleTopEntity,position: Int) {
            this.bean = bean
            view.root.isActivated = bean.collect
            val interpolation = if (bean.collect) 1F else 0F
            updateCardViewTopLeftCornerSize(interpolation)
            view.author.text = if (bean.author.isEmpty()) bean.shareUser else bean.author
            view.textHomeTopArticleTitle.text = bean.title
            view.textHomeTopArticleChapter.text = context.getString(
                R.string.homeFragment_chapter,
                bean.superChapterName,
                bean.chapterName
            )
            view.textHomeTopArticleTime.text = bean.niceShareDate
            view.newArticle.visibility = if (bean.fresh) View.VISIBLE else View.GONE
            if (bean.tags.isNotEmpty()){
                view.tag1.visibility =View.VISIBLE
                view.tag1.text = bean.tags[0].name
                if (bean.tags.size>1){
                    view.tag2.visibility =  View.VISIBLE
                    view.tag2.text = bean.tags[1].name
                }else{
                    view.tag2.visibility =  View.GONE
                }
            }else{
                view.tag1.visibility =  View.GONE
                view.tag2.visibility =  View.GONE
            }

            view.cardView.onClick {
                listener.onItemClicked(it, position,bean)
            }
        }

        override val reboundAbleView: View
            get() = view.cardView

        override fun onReboundOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float,
            currentTargetHasMetThresholdOnce: Boolean
        ) {
            if (currentTargetHasMetThresholdOnce) return

            val isStarred = bean.collect

            val interpolation = (currentSwipePercentage / swipeThreshold).coerceIn(0F, 1F)
            val adjustedInterpolation = abs((if (isStarred) 1F else 0F) - interpolation)
            updateCardViewTopLeftCornerSize(adjustedInterpolation)

            // Start the background animation once the threshold is met.
            val thresholdMet = currentSwipePercentage >= swipeThreshold
            val shouldStar = when {
                thresholdMet && isStarred -> false
                thresholdMet && !isStarred -> true
                else -> return
            }
            view.root.isActivated = shouldStar
        }

        override fun onRebounded() {
            val isCollect = bean.collect
            listener.onStatusChanged(bean, isCollect)
        }

        private fun updateCardViewTopLeftCornerSize(interpolation: Float) {
            view.cardView.apply {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(interpolation * starredCornerSize)
                    .build()
            }
        }
    }

    /**
     *普通文章
     **/
    inner class HomeCommentViewHolder(private val view: ItemHomeCommentLayoutBinding) :
        BaseViewHolder(view.root), ReboundingSwipeActionCallback.ReboundAbleViewHolder {
        private lateinit var bean: ArticleListBean

        init {
            view.run {
                this.root.background = HomeCollectSwipeActionDrawable(context)
            }
        }

        fun setData(bean: ArticleListBean,position: Int) {
            this.bean = bean
            view.root.isActivated = bean.collect
            val interpolation = if (bean.collect) 1F else 0F
            updateCardViewTopLeftCornerSize(interpolation)
            view.author.text = if (bean.author.isEmpty()) bean.shareUser else bean.author
            view.textHomeTopArticleTitle.text = bean.title
            view.textHomeTopArticleChapter.text = context.getString(
                R.string.homeFragment_chapter,
                bean.superChapterName,
                bean.chapterName
            )
            view.textHomeTopArticleTime.text = bean.niceShareDate
            view.newArticle.visibility = if (bean.fresh) View.VISIBLE else View.GONE
            if (bean.tags.isNotEmpty()){
                view.tag1.visibility =View.VISIBLE
                view.tag1.text = bean.tags[0].name
                if (bean.tags.size>1){
                    view.tag2.visibility =  View.VISIBLE
                    view.tag2.text = bean.tags[1].name
                }else{
                    view.tag2.visibility =  View.GONE
                }
            }else{
                view.tag1.visibility =  View.GONE
                view.tag2.visibility =  View.GONE
            }

            view.cardView.onClick {
                listener.onItemClicked(it,position, bean)
            }
        }

        override val reboundAbleView: View
            get() = view.cardView

        override fun onReboundOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float,
            currentTargetHasMetThresholdOnce: Boolean
        ) {
            if (currentTargetHasMetThresholdOnce) return

            val isStarred = bean.collect

            val interpolation = (currentSwipePercentage / swipeThreshold).coerceIn(0F, 1F)
            val adjustedInterpolation = abs((if (isStarred) 1F else 0F) - interpolation)
            updateCardViewTopLeftCornerSize(adjustedInterpolation)

            // Start the background animation once the threshold is met.
            val thresholdMet = currentSwipePercentage >= swipeThreshold
            val shouldStar = when {
                thresholdMet && isStarred -> false
                thresholdMet && !isStarred -> true
                else -> return
            }
            view.root.isActivated = shouldStar
        }

        override fun onRebounded() {
            val isCollect = bean.collect
            listener.onStatusChanged(bean, isCollect)
        }

        private fun updateCardViewTopLeftCornerSize(interpolation: Float) {
            view.cardView.apply {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(interpolation * starredCornerSize)
                    .build()
            }
        }
    }

    fun onHiddenChanged(isHidden: Boolean) {
        banner?.onHiddenChanged(isHidden)
    }

    interface HomeAdapterListener {
        fun onItemClicked(cardView: View, position: Int,bean: BaseHomeAllEntity)
        fun onStatusChanged(bean: BaseHomeAllEntity?, newValue: Boolean)
        fun onCollected(bean: BaseHomeAllEntity)
    }
}