package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.databinding.FragmentSearchResultBinding
import com.example.android.nixknewsapp.ui.main.adapters.ArticlePagingAdapter
import com.example.android.nixknewsapp.ui.main.adapters.NewsLoadStateAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class SearchResultFragment : Fragment() {
    private  val exploreViewModel: ExploreViewModel by activityViewModels()

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlePagingAdapter: ArticlePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exploreLayout.searchView.apply {
            setQuery(exploreViewModel.currentQuery.value, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    exploreViewModel.onSearchQuerySubmit(query)
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }

        articlePagingAdapter = ArticlePagingAdapter { view, article ->
            popupMenus(view, article)
        }
        articlePagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            rvSearch.apply {
                adapter = articlePagingAdapter.withLoadStateFooter(NewsLoadStateAdapter())
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                exploreViewModel.exploreNews.collectLatest {
                    articlePagingAdapter.submitData(it)
                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                articlePagingAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.source.refresh }
                    .filter { it.source.refresh is LoadState.NotLoading }
                    .collect {
                        if (exploreViewModel.pendingScrollToTopAfterNewQuery) {
                            rvSearch.scrollToPosition(0)
                            exploreViewModel.pendingScrollToTopAfterNewQuery = false
                        }
                        if (exploreViewModel.pendingScrollToTopAfterNewQuery && it.mediator?.refresh is LoadState.NotLoading) {
                            rvSearch.scrollToPosition(0)
                            exploreViewModel.pendingScrollToTopAfterNewQuery = false
                        }
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
                    exploreViewModel.saveArticle(article)
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