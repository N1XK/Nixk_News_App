package com.example.android.nixknewsapp.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import com.example.android.nixknewsapp.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {
    val savedArticles = repository.getSavedArticles()
}