package com.example.android.nixknewsapp.data.model

import androidx.paging.*
import androidx.room.withTransaction
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.utils.Constants.Companion.NETWORK_PAGING_SIZE
import com.example.android.nixknewsapp.utils.Resource
import com.example.android.nixknewsapp.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class Repository @Inject constructor(
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
                pageSize = NETWORK_PAGING_SIZE,
                maxSize = 50,
                enablePlaceholders = false),
            remoteMediator = TrendingRemoteMediator(service, database),
            pagingSourceFactory = { articleDao.getTrendingNews() }
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

    fun getGeneralNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(GENERAL_NEWS_TAG, service) }
        ).flow

    fun getBusinessNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(BUSINESS_NEWS_TAG, service) }
        ).flow

    fun getSportsNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(SPORTS_NEWS_TAG, service) }
        ).flow

    fun getTechNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(TECH_NEWS_TAG, service) }
        ).flow

    fun getHealthNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(HEALTH_NEWS_TAG, service) }
        ).flow

    fun getScienceNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(SCIENCE_NEWS_TAG, service) }
        ).flow

    fun getPoliticsNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(POLITICS_NEWS_TAG, service) }
        ).flow

    fun getEntertainmentNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(ENTERTAINMENT_NEWS_TAG, service) }
        ).flow

    fun getFoodNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(FOOD_NEWS_TAG, service) }
        ).flow

    fun getTravelNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(TRAVEL_NEWS_TAG, service) }
        ).flow

    fun getABCNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(ABC_NEWS_TAG, service) }
        ).flow

    fun getAljazeeraNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(ALJAZEERA_NEWS_TAG, service) }
        ).flow

    fun getUSATodayNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(USATODAY_NEWS_TAG, service) }
        ).flow

    fun getAppleInsiderNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(APPLEINSIDER_NEWS_TAG, service) }
        ).flow

    fun getArchDailyNews() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(ARCHDAILY_NEWS_TAG, service) }
        ).flow

    fun getSmashingMagazine() =
        Pager(
            config = PagingConfig(pageSize = NETWORK_PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { NewsPagingSource(SMASHING_MAGAZINE_TAG, service) }
        ).flow

    fun getSavedArticles() : Flow<List<Article>> =
        articleDao.getSavedArticles()

    suspend fun updateArticle(article: Article) {
        articleDao.updateArticle(article)
    }
}

private const val GENERAL_NEWS_TAG = "general"
private const val BUSINESS_NEWS_TAG = "business"
private const val SPORTS_NEWS_TAG = "sports"
private const val TECH_NEWS_TAG = "tech"
private const val HEALTH_NEWS_TAG = "health"
private const val SCIENCE_NEWS_TAG = "science"
private const val POLITICS_NEWS_TAG = "politics"
private const val ENTERTAINMENT_NEWS_TAG = "entertainment"
private const val FOOD_NEWS_TAG = "food"
private const val TRAVEL_NEWS_TAG = "travel"
private const val ABC_NEWS_TAG = "abc"
private const val ALJAZEERA_NEWS_TAG = "aljazeera"
private const val USATODAY_NEWS_TAG = "usaToday"
private const val APPLEINSIDER_NEWS_TAG = "appleInsider"
private const val ARCHDAILY_NEWS_TAG = "archDaily"
private const val SMASHING_MAGAZINE_TAG = "smashingMagazine"


