package com.example.android.nixknewsapp.ui.main.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.databinding.ItemsArticleBinding
import com.example.android.nixknewsapp.utils.Extensions.formatTimeAgo

class ArticlePagingAdapter(
    private val onMenuClicked: (View, Article) -> Unit,
    ) : PagingDataAdapter<Article, ArticlePagingAdapter.ArticleViewHolder>(
    object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.uuid == newItem.uuid && oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
){
    inner class ArticleViewHolder(private val binding: ItemsArticleBinding
    ) : RecyclerView.ViewHolder(binding.root)  {

        init {
            binding.ivDots.setOnClickListener { view ->
                onMenuClicked(view, getItem(bindingAdapterPosition)!!)
            }
        }
        fun bind(article: Article, context: Context) {
            Glide.with(context)
                .load(article.image_url)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.not_found_404)
                .into(binding.ivArticle)
            binding.apply {
                // TODO("to make better logic")
                if (!article.categories.isNullOrEmpty()) {
                    tvInnerCategory1.text = article.categories.first().toString()
                    if (article.categories.size > 2) {
                        tvInnerCategory2.text = article.categories[1]
                    } else {
                        tvInnerCategory2.visibility = View.GONE
                    }
                } else {
                    tvInnerCategory1.text = "general"
                    tvInnerCategory2.visibility = View.GONE
                }
                tvArticleTitle.text = article.title
                tvTime.text = article.published_at?.formatTimeAgo() ?: ""
            }
        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = getItem(position)
        if (currentArticle != null) {
            holder.bind(currentArticle, holder.itemView.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemsArticleBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
}