package com.example.android.nixknewsapp.data.model

import com.example.android.nixknewsapp.data.api.ArticleDto

data class NewsResponse(
    val data: List<ArticleDto>,
)