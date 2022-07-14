package com.example.jyghiretest.product.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jyghiretest.databinding.FragmentProductHomeBinding
import com.example.jyghiretest.safeCollect
import com.google.android.material.tabs.TabLayoutMediator

class ProductHomeFragment : Fragment() {

    private var _binding: FragmentProductHomeBinding? = null
    private val binding: FragmentProductHomeBinding get() = _binding!!

    private val viewModel: ProductHomeViewModel by viewModels()

    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
        setUpTabLayout()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.safeCollect(viewLifecycleOwner) {
            val categories = it.categories
            categoriesAdapter.updateCategories(categories)
        }
    }

    private fun setUpViewPager() {
        categoriesAdapter = CategoriesAdapter(this)
        binding.viewpagerCategories.adapter = categoriesAdapter
    }

    private fun setUpTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewpagerCategories) { tab, position ->
            tab.text = categoriesAdapter.getTitleByPosition(position)
        }.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = ProductHomeFragment()

    }

}
