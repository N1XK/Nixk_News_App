package com.example.android.nixknewsapp.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.data.db.ArticleDatabase
import com.example.android.nixknewsapp.data.mediators.ExploreRemoteMediator
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.paging.NewsPagingSource
import com.example.android.nixknewsapp.utils.Constants
import com.example.android.nixknewsapp.utils.Constants.Companion.NETWORK_PAGING_SIZE
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ExploreRepository @Inject constructor(
    private val service: NewsApi,
    private val database: ArticleDatabase
) {
    private val articleDao = database.articleDao()
    fun getNewsBySource(source: String) =
        Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(source, service) }
        ).flow

    @OptIn(ExperimentalPagingApi::class)
    fun getExploreNews(query: String): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGING_SIZE,
                maxSize = 50,
                enablePlaceholders = false),
            remoteMediator = ExploreRemoteMediator(query, service, database),
            pagingSourceFactory = { articleDao.getExploreNews(query) }
        ).flow

    fun getSavedArticles() : Flow<List<Article>> =
        articleDao.getSavedArticles()

    suspend fun updateArticle(article: Article) {
        articleDao.updateArticle(article)
    }
}