package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
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

    val generalNews = repository.getGeneralNews().cachedIn(viewModelScope)

    val businessNews = repository.getBusinessNews().cachedIn(viewModelScope)

    val sportsNews = repository.getSportsNews().cachedIn(viewModelScope)

    val techNews = repository.getTechNews().cachedIn(viewModelScope)

    val healthNews = repository.getHealthNews().cachedIn(viewModelScope)

    val scienceNews = repository.getScienceNews().cachedIn(viewModelScope)

    val politicsNews = repository.getPoliticsNews().cachedIn(viewModelScope)

    val entertainmentNews = repository.getEntertainmentNews().cachedIn(viewModelScope)

    val foodNews = repository.getFoodNews().cachedIn(viewModelScope)

    val travelNews = repository.getTravelNews().cachedIn(viewModelScope)

    val savedArticles = repository.getSavedArticles()

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.updateArticle(article)
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }
}