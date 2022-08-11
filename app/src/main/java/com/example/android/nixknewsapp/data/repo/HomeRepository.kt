package com.example.android.nixknewsapp.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.data.db.ArticleDatabase
import com.example.android.nixknewsapp.data.mediators.TrendingRemoteMediator
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.model.TopStory
import com.example.android.nixknewsapp.data.paging.NewsPagingSource
import com.example.android.nixknewsapp.utils.Constants
import com.example.android.nixknewsapp.utils.Resource
import com.example.android.nixknewsapp.utils.networkBoundResource
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class HomeRepository @Inject constructor(
    private val service: NewsApi,
    private val database: ArticleDatabase
) {
    private val articleDao = database.articleDao()
    fun getTopStories(
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<Article>>> =

        networkBoundResource(
            query = {
                articleDao.getTopStories()
            },
            fetch = {
                val response = service.getTopStories()
                response.data
            },
            saveFetchResult = { serverArticleList ->
                val topStoryArticles = serverArticleList.map { article ->
                    Article(
                        categories = article.categories,
                        image_url = article.image_url,
                        language = article.language,
                        locale = article.locale,
                        published_at = article.published_at,
                        title = article.title,
                        url = article.url,
                        uuid = article.uuid
                    )
                }
                val topStories = topStoryArticles.map { article ->
                    TopStory(uuid_top_story = article.uuid)
                }

                //Delete and Save to the Database
                database.withTransaction {
                    articleDao.deleteTopStories()
                    articleDao.insertArticles(topStoryArticles)
                    articleDao.insertTopStories(topStories)
                }
            },
//          TODO("Need to ask")
            shouldFetch = { true },
            onFetchSuccess = onFetchSuccess,

            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchFailed(t)
            }
        )

    @OptIn(ExperimentalPagingApi::class)
    fun getTrendingNews(): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(
                pageSize = Constants.NETWORK_PAGING_SIZE,
                maxSize = 50,
                enablePlaceholders = false),
            remoteMediator = TrendingRemoteMediator(service, database),
            pagingSourceFactory = { articleDao.getTrendingNews() }
        ).flow

    fun getNewsByCategory(tag:String) =
        Pager(
            config = PagingConfig(pageSize = Constants.NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(tag, service) }
        ).flow

    fun getSavedArticles() : Flow<List<Article>> =
        articleDao.getSavedArticles()

    suspend fun updateArticle(article: Article) {
        articleDao.updateArticle(article)
    }

}