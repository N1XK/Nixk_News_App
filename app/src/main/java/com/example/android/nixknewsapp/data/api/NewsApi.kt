package com.example.android.nixknewsapp.data.api

import com.example.android.nixknewsapp.data.model.NewsResponse
import com.example.android.nixknewsapp.utils.Constants.Companion.API_KEY
//import com.example.android.nixknewsapp.utils.Constants.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("top")
    suspend fun getTopStories(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("locale")
        local: String = "us",
        @Query("limit")
        limit: Int = 5
    ): NewsResponse

    @GET("all")
    suspend fun getTrendingNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getGeneralNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "general",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getBusinessNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "business",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getSportsNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "sports",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getTechNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "tech",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getHealthNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "health",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getScienceNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "science",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getPoliticsNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "politics",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getEntertainmentNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "entertainment",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getFoodNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "food",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getTravelNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("language")
        language: String = "en",
        @Query("categories")
        category: String = "travel",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getExploreNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("search")
        searchQuery: String,
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getABCNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "abcnews.go.com-1",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getAljazeeraNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "aljazeera.com-1",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getUSATodayNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "usatoday.com-2",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getAppleInsiderNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "appleinsider.com-1",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getArchDailyNews(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "archdaily.com-1",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse

    @GET("all")
    suspend fun getSmashingMagazine(
        @Query("api_token")
        apiToken: String = API_KEY,
        @Query("source_id")
        sourceId: String = "smashingmagazine.com-1",
        @Query("language")
        language: String = "en",
        @Query("page")
        page: Int = 1
    ): NewsResponse
}