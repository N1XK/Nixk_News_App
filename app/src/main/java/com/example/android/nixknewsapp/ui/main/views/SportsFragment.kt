package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.databinding.FragmentSportsBinding
import com.example.android.nixknewsapp.ui.main.adapters.ArticlePagingAdapter
import com.example.android.nixknewsapp.ui.main.adapters.NewsLoadStateAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SportsFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentSportsBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlePagingAdapter: ArticlePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlePagingAdapter = ArticlePagingAdapter { view, article ->
            popupMenus(view, article)
        }
        articlePagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.apply {
            rvSports.apply {
                adapter = articlePagingAdapter.withLoadStateFooter(NewsLoadStateAdapter())
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                homeViewModel.sportsNews.collectLatest { data ->
                    val result = data ?: return@collectLatest
                    articlePagingAdapter.submitData(result)
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                articlePagingAdapter.loadStateFlow.collect { loadState ->
                    rvSports.isVisible = loadState.source.refresh is LoadState.NotLoading
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

    private fun popupMenus(view: View, article: Article) {
        val popupMenus = PopupMenu(requireContext(), view)
        popupMenus.inflate(R.menu.popup_menu)
        popupMenus.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.menu_save -> {
                    article.isSaved = true
                    homeViewModel.saveArticle(article)
                    Toast.makeText(context, "Saved Deleted.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_delete -> {
                    Toast.makeText(context, "Article Deleted.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_share -> {
                    Toast.makeText(context, "Article Shared.", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> true
            }
        }
        popupMenus.show()
    }
}