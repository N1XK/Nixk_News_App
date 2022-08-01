package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Category
import com.example.android.nixknewsapp.databinding.FragmentExploreBinding
import com.example.android.nixknewsapp.ui.main.adapters.ArticlePagingAdapter
import com.example.android.nixknewsapp.ui.main.adapters.CategoriesAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private val exploreViewModel: ExploreViewModel by activityViewModels()

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var articlePagingAdapter: ArticlePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = listOf(
            Category(R.drawable.business, "Business"),
            Category(R.drawable.sports, "Sports"),
            Category(R.drawable.technology, "Technology"),
            Category(R.drawable.health, "Health"),
            Category(R.drawable.science, "Science"),
            Category(R.drawable.politics, "Politics"),
            Category(R.drawable.entertainment, "Entertainment"),
            Category(R.drawable.food, "Food"),
            Category(R.drawable.travel, "Travel"),
        )

        val sources = listOf(
            Category(R.drawable.abc_news, "ABC News"),
            Category(R.drawable.aljazeera, "Aljazeera"),
            Category(R.drawable.usa_today, "USA Today"),
            Category(R.drawable.appleinsider, "AppleInsider"),
            Category(R.drawable.archdaily, "Arch Daily"),
            Category(R.drawable.smashingmagazine, "Smashing Magazine")
        )

        categoriesAdapter = CategoriesAdapter(categories) { category ->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailsCategoriesFragment(category)
            findNavController().navigate(action)
        }
        binding.apply {
            rvCategories.apply {
                adapter = categoriesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }

        val sourcesAdapter = CategoriesAdapter(sources) { source ->
            val action = ExploreFragmentDirections.actionExploreFragmentToDetailsCategoriesFragment(source)
            findNavController().navigate(action)

        }
        binding.apply {
            rvSources.apply {
                adapter = sourcesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
        }

        binding.exploreLayout.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    exploreViewModel.onSearchQuerySubmit(query)
                    findNavController().navigate(R.id.action_exploreFragment_to_searchResultFragment)
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    return false
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun performOptionsMenuClick(position: Int) {
//        val popupMenu = PopupMenu(requireContext(), binding.rvSearch[position].findViewById(R.id.imageView))
//        popupMenu.inflate(R.menu.popup_menu)
//        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
//            override fun onMenuItemClick(item: MenuItem?): Boolean {
//                when (item?.itemId) {
//                    R.id.menu_save -> {
//                        Toast.makeText(requireContext(), "Article Saved.", Toast.LENGTH_SHORT).show()
//                        return true
//                    }
//                    R.id.menu_delete -> {
//                        Toast.makeText(requireContext(), "Article Deleted.", Toast.LENGTH_SHORT).show()
//                        return true
//                    }
//                }
//                return false
//            }
//        })
//        popupMenu.show()
//    }
}