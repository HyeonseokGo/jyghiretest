package com.example.jyghiretest.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jyghiretest.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViewPager()
        setUpTabLayout()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewpagerProducts) { tab, position ->
            tab.text =
                HomeViewPagerFragments.getByPosition(position).requireTitle(requireContext())
        }.attach()
    }

    private fun setUpViewPager() {
        binding.viewpagerProducts.isUserInputEnabled = false
        binding.viewpagerProducts.adapter = HomeFragmentsAdapter(requireActivity())
    }

}
