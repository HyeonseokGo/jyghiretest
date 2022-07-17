package com.example.jyghiretest.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.jyghiretest.MainViewModel
import com.example.jyghiretest.databinding.FragmentFavoriteProductsBinding
import com.example.jyghiretest.product.list.ProductItemAdapter
import com.example.jyghiretest.safeCollect
import com.example.jyghiretest.safeConsumeCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteProductsFragment : Fragment() {

    private var _binding: FragmentFavoriteProductsBinding? = null
    private val binding: FragmentFavoriteProductsBinding get() = _binding!!

    private val viewModel: FavoriteProductsViewModel by viewModels()

    private val navigator: MainViewModel by activityViewModels()

    private lateinit var adapter: ProductItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteProductsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditText()
        setUpRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpEditText() {
        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text?.toString() ?: ""
            viewModel.search(query)
        }
    }

    private fun setUpRecyclerView() {
        adapter = ProductItemAdapter(
            onItemClick = navigator::navigateToProductDetail,
            onFavoriteClick = viewModel::toggleFavorite
        )
        binding.recyclerViewFavoriteProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.lastSearchQuery.safeConsumeCollect(viewLifecycleOwner) {
            binding.editTextSearch.setText(it)
        }

        viewModel.state.safeCollect(viewLifecycleOwner) {
            adapter.submitList(it.products)
        }
    }

    companion object {

        fun newInstance() = FavoriteProductsFragment()

    }

}
