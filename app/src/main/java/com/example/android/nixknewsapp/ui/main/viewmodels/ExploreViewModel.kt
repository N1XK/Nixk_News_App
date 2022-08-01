package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.android.nixknewsapp.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: Repository,
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
        currentQuery.value = query
        pendingScrollToTopAfterNewQuery = true
    }

    val businessNews = repository.getBusinessNews().cachedIn(viewModelScope)

    val sportsNews = repository.getSportsNews().cachedIn(viewModelScope)

    val techNews = repository.getTechNews().cachedIn(viewModelScope)

    val healthNews = repository.getHealthNews().cachedIn(viewModelScope)

    val scienceNews = repository.getScienceNews().cachedIn(viewModelScope)

    val politicsNews = repository.getPoliticsNews().cachedIn(viewModelScope)

    val entertainmentNews = repository.getEntertainmentNews().cachedIn(viewModelScope)

    val foodNews = repository.getFoodNews().cachedIn(viewModelScope)

    val travelNews = repository.getTravelNews().cachedIn(viewModelScope)

    val abc = repository.getABCNews().cachedIn(viewModelScope)

    val aljazeera = repository.getAljazeeraNews().cachedIn(viewModelScope)

    val usaToday = repository.getUSATodayNews().cachedIn(viewModelScope)

    val appleInsider = repository.getAppleInsiderNews().cachedIn(viewModelScope)

    val archDaily = repository.getArchDailyNews().cachedIn(viewModelScope)

    val smashingMagazine = repository.getSmashingMagazine().cachedIn(viewModelScope)

}