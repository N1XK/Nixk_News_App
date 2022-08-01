package com.example.android.nixknewsapp.data.model

import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.nixknewsapp.data.api.NewsApi
import com.example.android.nixknewsapp.utils.Constants.Companion.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsPagingSource @Inject constructor(
    private val chooseNews: String,
    private val service: NewsApi,
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = when (chooseNews) {
                "general" -> service.getGeneralNews(page = position)
                "business" -> service.getBusinessNews(page = position)
                "sports" -> service.getSportsNews(page = position)
                "tech" -> service.getTechNews(page = position)
                "health" -> service.getHealthNews(page = position)
                "science" -> service.getScienceNews(page = position)
                "politics" -> service.getPoliticsNews(page = position)
                "entertainment" -> service.getEntertainmentNews(page = position)
                "food" -> service.getFoodNews(page = position)
                "travel" -> service.getTravelNews(page = position)
                "abc" -> service.getABCNews(page = position)
                "aljazeera" -> service.getAljazeeraNews(page = position)
                "usaToday" -> service.getUSATodayNews(page = position)
                "appleInsider" -> service.getAppleInsiderNews(page = position)
                "archDaily" -> service.getArchDailyNews(page = position)
                "smashingMagazine" -> service.getSmashingMagazine(page = position)
                else -> null
            }
            val articlesDto = response?.data ?: listOf()
            val articles = articlesDto.map { article ->
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
            LoadResult.Page(
                data = articles,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (articles.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}