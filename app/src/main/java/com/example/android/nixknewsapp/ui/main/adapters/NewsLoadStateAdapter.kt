package com.example.android.nixknewsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nixknewsapp.databinding.LoadStateFooterBinding

class NewsLoadStateAdapter : LoadStateAdapter<NewsLoadStateAdapter.LoadStateViewHolder>() {
    class LoadStateViewHolder(private val binding: LoadStateFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.loading.isVisible = loadState is LoadState.Loading
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}