package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.data.repo.ExploreRepository
import com.example.android.nixknewsapp.data.repo.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import com.example.android.nixknewsapp.utils.Constants
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: ExploreRepository,
    private val homeRepo:HomeRepository
) : ViewModel() {
//    private val currentQuery = state.getLiveData<String?>("currentQuery", null)
    var currentQuery =  MutableLiveData<String>()

    val exploreNews = currentQuery.asFlow().flatMapLatest { query ->
        query?.let {
            repository.getExploreNews(query)
        } ?: emptyFlow()
    }.cachedIn(viewModelScope)

    var pendingScrollToTopAfterNewQuery = false

    fun onSearchQuerySubmit(query: String?) {
        query?.let {
            currentQuery.value = it
        }
        pendingScrollToTopAfterNewQuery = true
    }

    val businessNews = homeRepo.getNewsByCategory(Constants.BUSINESS_NEWS_TAG).cachedIn(viewModelScope)

    val sportsNews = homeRepo.getNewsByCategory(Constants.SPORTS_NEWS_TAG).cachedIn(viewModelScope)

    val techNews = homeRepo.getNewsByCategory(Constants.TECH_NEWS_TAG).cachedIn(viewModelScope)

    val healthNews = homeRepo.getNewsByCategory(Constants.HEALTH_NEWS_TAG).cachedIn(viewModelScope)

    val scienceNews = homeRepo.getNewsByCategory(Constants.SCIENCE_NEWS_TAG).cachedIn(viewModelScope)

    val politicsNews = homeRepo.getNewsByCategory(Constants.POLITICS_NEWS_TAG).cachedIn(viewModelScope)

    val entertainmentNews = homeRepo.getNewsByCategory(Constants.ENTERTAINMENT_NEWS_TAG).cachedIn(viewModelScope)

    val foodNews = homeRepo.getNewsByCategory(Constants.FOOD_NEWS_TAG).cachedIn(viewModelScope)

    val travelNews = homeRepo.getNewsByCategory(Constants.TRAVEL_NEWS_TAG).cachedIn(viewModelScope)

    val abc = repository.getNewsBySource(ABC_NEWS_TAG).cachedIn(viewModelScope)

    val aljazeera = repository.getNewsBySource(ALJAZEERA_NEWS_TAG).cachedIn(viewModelScope)

    val usaToday = repository.getNewsBySource(USATODAY_NEWS_TAG).cachedIn(viewModelScope)

    val appleInsider = repository.getNewsBySource(APPLEINSIDER_NEWS_TAG).cachedIn(viewModelScope)

    val archDaily = repository.getNewsBySource(ARCHDAILY_NEWS_TAG).cachedIn(viewModelScope)

    val smashingMagazine = repository.getNewsBySource(SMASHING_MAGAZINE_TAG).cachedIn(viewModelScope)

    val savedArticles = repository.getSavedArticles()

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.updateArticle(article)
    }
}

private const val ABC_NEWS_TAG = "abc"
private const val ALJAZEERA_NEWS_TAG = "aljazeera"
private const val USATODAY_NEWS_TAG = "usaToday"
private const val APPLEINSIDER_NEWS_TAG = "appleInsider"
private const val ARCHDAILY_NEWS_TAG = "archDaily"
private const val SMASHING_MAGAZINE_TAG = "smashingMagazine"