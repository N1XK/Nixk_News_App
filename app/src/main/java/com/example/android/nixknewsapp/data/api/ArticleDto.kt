package com.example.android.nixknewsapp.data.api

data class ArticleDto(
    val categories: List<String>?,
    val image_url: String?,
    val language: String?,
    val locale: String?,
    val published_at: String?,
    val title: String?,
    val url: String,
    val uuid: String,
    val relevance_score: Float? = null
)
