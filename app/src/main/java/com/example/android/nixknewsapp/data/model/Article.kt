package com.example.android.nixknewsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    val categories: List<String>?,
    val image_url: String?,
    val language: String?,
    val locale: String?,
    val published_at: String?,
    val title: String?,
    val url: String?,
    @PrimaryKey val uuid: String,
    val relevance_score: Float? = null
)

@Entity(tableName = "top_stories")
data class TopStory(
    val uuid_top_story: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)

@Entity(tableName = "trending_news")
data class TrendingNews(
    val uuid_trending_news: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
)

@Entity(tableName = "explore_news", primaryKeys = ["searchQuery", "uuid_explore_news"])
data class ExploreNews(
    val searchQuery: String,
    val uuid_explore_news: String,
    val relevance_score_explore: Float?
)
