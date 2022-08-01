package com.example.android.nixknewsapp.data.model

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM top_stories INNER JOIN articles ON uuid_top_story = uuid ORDER BY id")
    fun getTopStories(): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopStories(articles: List<TopStory>)

    @Query("DELETE FROM top_stories")
    suspend fun deleteTopStories()

    @Query("SELECT * FROM trending_news INNER JOIN articles ON uuid_trending_news = uuid")
    fun getTrendingNews(): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrendingNews(articles: List<TrendingNews>)

    @Query("DELETE FROM trending_news")
    suspend fun deleteTrendingNews()

    @Query("SELECT * FROM explore_news INNER JOIN articles " +
            "ON uuid_explore_news = uuid WHERE searchQuery = :query " +
            "ORDER BY relevance_score_explore DESC")
    fun getExploreNews(query: String): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExploreNews(articles: List<ExploreNews>)

    @Query("DELETE FROM explore_news")
    suspend fun deleteExploreNews()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)
}