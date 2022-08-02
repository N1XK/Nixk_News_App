package com.example.android.nixknewsapp.ui.main.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Article
import com.example.android.nixknewsapp.databinding.FragmentFollowingBinding
import com.example.android.nixknewsapp.ui.main.adapters.CategoriesAdapter
import com.example.android.nixknewsapp.ui.main.adapters.SavedAdapter
import com.example.android.nixknewsapp.ui.main.viewmodels.FollowingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    private val followingViewModel: FollowingViewModel by viewModels()

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var savedAdapter: SavedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedAdapter = SavedAdapter { view, article ->
            popupMenus(view, article)
        }
        savedAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.apply {
            rvArticle.apply {
                adapter = savedAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                followingViewModel.savedArticles.collect { data ->
                    if (data != null) {
                        cardView.visibility = View.GONE
                    }
                    val result = data ?: return@collect
                    savedAdapter.submitList(result)
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
        popupMenus.menu[0].isVisible = false
        popupMenus.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
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