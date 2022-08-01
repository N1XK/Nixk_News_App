package com.example.android.nixknewsapp.ui.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.nixknewsapp.ui.main.views.*
import com.example.android.nixknewsapp.utils.Constants.Companion.NUM_TABS

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TrendingFragment()
            1 -> GeneralFragment()
            2 -> BusinessFragment()
            3 -> SportsFragment()
            4 -> TechFragment()
            5 -> HealthFragment()
            6 -> ScienceFragment()
            7 -> PoliticsFragment()
            8 -> EntertainmentFragment()
            9 -> FoodFragment()
            else -> TravelFragment()
        }
    }
}