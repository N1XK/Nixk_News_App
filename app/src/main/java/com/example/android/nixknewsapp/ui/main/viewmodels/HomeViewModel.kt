package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.repo.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel() {

    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    var pendingTopStoryToTop = false

    val topStories =
       repository.getTopStories(
        onFetchSuccess = { pendingTopStoryToTop = true },
        onFetchFailed = { t ->
            viewModelScope.launch { eventChannel.send(Event.ShowErrorMessage(t)) }
        }
    )

    val trendingNews = repository.getTrendingNews()

    val generalNews = repository.getNewsByCategory(GENERAL_NEWS_TAG).cachedIn(viewModelScope)

    val businessNews = repository.getNewsByCategory(BUSINESS_NEWS_TAG).cachedIn(viewModelScope)

    val sportsNews = repository.getNewsByCategory(SPORTS_NEWS_TAG).cachedIn(viewModelScope)

    val techNews = repository.getNewsByCategory(TECH_NEWS_TAG).cachedIn(viewModelScope)

    val healthNews = repository.getNewsByCategory(HEALTH_NEWS_TAG).cachedIn(viewModelScope)

    val scienceNews = repository.getNewsByCategory(SCIENCE_NEWS_TAG).cachedIn(viewModelScope)

    val politicsNews = repository.getNewsByCategory(POLITICS_NEWS_TAG).cachedIn(viewModelScope)

    val entertainmentNews = repository.getNewsByCategory(ENTERTAINMENT_NEWS_TAG).cachedIn(viewModelScope)

    val foodNews = repository.getNewsByCategory(FOOD_NEWS_TAG).cachedIn(viewModelScope)

    val travelNews = repository.getNewsByCategory(TRAVEL_NEWS_TAG).cachedIn(viewModelScope)

    val savedArticles = repository.getSavedArticles()

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.updateArticle(article)
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
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
