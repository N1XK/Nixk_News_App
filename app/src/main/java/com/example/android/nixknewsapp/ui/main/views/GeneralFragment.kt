package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nixknewsapp.databinding.FragmentGeneralBinding
import com.example.android.nixknewsapp.ui.main.adapters.ArticlePagingAdapter
import com.example.android.nixknewsapp.ui.main.adapters.NewsLoadStateAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GeneralFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentGeneralBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlePagingAdapter: ArticlePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlePagingAdapter = ArticlePagingAdapter()
        articlePagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.apply {
            rvGeneral.apply {
                adapter = articlePagingAdapter.withLoadStateFooter(NewsLoadStateAdapter())
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                homeViewModel.generalNews.collectLatest { data ->
                    val result = data ?: return@collectLatest
                    articlePagingAdapter.submitData(result)
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                articlePagingAdapter.loadStateFlow.collect { loadState ->
                    rvGeneral.isVisible = loadState.source.refresh is LoadState.NotLoading
                    loading.isVisible = loadState.source.refresh is LoadState.Loading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                            && articlePagingAdapter.itemCount == 0
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}