package com.example.jyghiretest.home

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeFragmentsAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = HomeViewPagerFragments.values().size

    override fun createFragment(position: Int) =
        HomeViewPagerFragments.getByPosition(position).factory()

}