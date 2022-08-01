package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nixknewsapp.databinding.FragmentDetailsCategoriesBinding
import com.example.android.nixknewsapp.ui.main.adapters.ArticlePagingAdapter
import com.example.android.nixknewsapp.ui.main.adapters.NewsLoadStateAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

@AndroidEntryPoint
class DetailsCategoriesFragment : Fragment() {
    private val exploreViewModel: ExploreViewModel by activityViewModels()

    private var _binding: FragmentDetailsCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var articlePagingAdapter: ArticlePagingAdapter
    private val navigationArgs: DetailsCategoriesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = navigationArgs.category.title
        val imageId = navigationArgs.category.imageId

        articlePagingAdapter = ArticlePagingAdapter()
        articlePagingAdapter.refresh()
        articlePagingAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            tvTitle.text = title
            Glide.with(requireContext()).load(imageId).into(imageView)
            rvArticle.apply {
                adapter = articlePagingAdapter.withLoadStateFooter(NewsLoadStateAdapter())
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                //It's not best practice cus I'm lazy :(
                val news = when (title) {
                    "Business" -> exploreViewModel.businessNews
                    "Sports" -> exploreViewModel.sportsNews
                    "Technology" -> exploreViewModel.techNews
                    "Health" -> exploreViewModel.healthNews
                    "Science" -> exploreViewModel.scienceNews
                    "Politics" -> exploreViewModel.politicsNews
                    "Entertainment" -> exploreViewModel.entertainmentNews
                    "Food" -> exploreViewModel.foodNews
                    "Travel" -> exploreViewModel.travelNews
                    "ABC News" -> exploreViewModel.abc
                    "Aljazeera" -> exploreViewModel.aljazeera
                    "USA Today" -> exploreViewModel.usaToday
                    "AppleInsider" -> exploreViewModel.appleInsider
                    "Arch Daily" -> exploreViewModel.archDaily
                    "Smashing Magazine" -> exploreViewModel.smashingMagazine
                    else -> emptyFlow()
                }
                news.collectLatest { data ->
                    val result = data ?: return@collectLatest
                    articlePagingAdapter.submitData(result)
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                articlePagingAdapter.loadStateFlow.collect { loadState ->
                    rvArticle.isVisible = loadState.source.refresh is LoadState.NotLoading
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