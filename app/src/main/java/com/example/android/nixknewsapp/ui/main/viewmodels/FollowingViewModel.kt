package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import com.example.android.nixknewsapp.data.repo.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val repository: HomeRepository
): ViewModel() {
    val savedArticles = repository.getSavedArticles()
}