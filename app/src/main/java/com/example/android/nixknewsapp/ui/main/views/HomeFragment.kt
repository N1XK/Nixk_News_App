package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.databinding.FragmentHomeBinding
import com.example.android.nixknewsapp.ui.main.adapters.TopStoriesAdapter
import com.example.android.nixknewsapp.ui.main.adapters.ViewPagerAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.HomeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        setupTopStoriesViewPager()

        val tabs = context?.resources?.getStringArray(R.array.tabs)
        //Setup TabLayout <Test>
        val viewPager = binding.vpCategories
        val tabLayout = binding.tabLayout
        binding.vpCategories.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs?.get(position)
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupTopStoriesViewPager() {
        val topStoriesAdapter = TopStoriesAdapter()
        topStoriesAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            vpTopStories.apply {
                offscreenPageLimit = 1
                val recyclerView = getChildAt(0) as RecyclerView
                recyclerView.apply {
                    setPadding(120, 0, 120, 0)
                    clipToPadding = false
                }
                adapter = topStoriesAdapter
                orientation = ViewPager2.ORIENTATION_HORIZONTAL
                if (homeViewModel.pendingTopStoryToTop) {
                    currentItem = 0
                    homeViewModel.pendingTopStoryToTop = false
                    }
                }
            indicator.setViewPager(vpTopStories)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                homeViewModel.topStories.collect {
                    val result = it ?: return@collect
                    topStoriesAdapter.submitList(result.data)
                }
            }
        }
}