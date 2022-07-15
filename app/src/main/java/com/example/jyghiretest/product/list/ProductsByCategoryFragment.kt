package com.example.jyghiretest.product.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jyghiretest.databinding.FragmentProductsByCategoryBinding
import com.example.jyghiretest.safeCollect
import dagger.hilt.android.AndroidEntryPoint

const val CATEGORY_KEY = "CATEGORY_KEY"

@AndroidEntryPoint
class ProductsByCategoryFragment : Fragment() {

    private var _binding: FragmentProductsByCategoryBinding? = null
    private val binding: FragmentProductsByCategoryBinding get() = _binding!!

    private val viewModel: ProductsByCategoryViewModel by viewModels()

    private lateinit var adapter: ProductsByCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsByCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        adapter = ProductsByCategoryAdapter(
            onItemClick = {},
            onFavoriteClick = {}
        )
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.state.safeCollect(viewLifecycleOwner) {
            adapter.submitList(it.products)
        }
    }


    companion object {
        fun newInstance(key: String) = ProductsByCategoryFragment().apply {
            arguments = Bundle().apply {
                putString(CATEGORY_KEY, key)
            }
        }
    }

}