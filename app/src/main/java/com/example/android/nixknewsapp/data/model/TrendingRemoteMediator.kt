package com.example.android.nixknewsapp.data.model

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.utils.Constants.Companion.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TrendingRemoteMediator @Inject constructor(
    private val service: NewsApi,
    private val database: ArticleDatabase
) : RemoteMediator<Int, Article>() {

    private val articleDao = database.articleDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = service.getTrendingNews(page = page)
            val serverTrendingNews = response.data
            val endOfPaginationReached = serverTrendingNews.isEmpty()

            val trendingNewsArticles = serverTrendingNews.map { article ->
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
            val trendingNews = trendingNewsArticles.map { article ->
                TrendingNews(uuid_trending_news = article.uuid)
            }


            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    articleDao.deleteTrendingNews()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = serverTrendingNews.map { article ->
                    RemoteKeys(
                        uuid = article.uuid,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertAll(keys)
                articleDao.insertArticles(trendingNewsArticles)
                articleDao.insertTrendingNews(trendingNews)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                remoteKeysDao.getRemoteKeysUuid(article.uuid)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                remoteKeysDao.getRemoteKeysUuid(article.uuid)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.uuid?.let { uuid ->
                remoteKeysDao.getRemoteKeysUuid(uuid)
            }
        }
    }
}