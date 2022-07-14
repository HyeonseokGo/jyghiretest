package com.example.jyghiretest.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeFragmentsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = HomeViewPagerFragments.values().size

    override fun createFragment(position: Int) =
        HomeViewPagerFragments.getByPosition(position).factory()

}