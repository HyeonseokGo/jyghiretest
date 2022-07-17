package com.example.jyghiretest.product.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.jyghiretest.R
import com.example.jyghiretest.databinding.FragmentProductDetailBinding
import com.example.jyghiretest.model.Product
import com.example.jyghiretest.product.model.formattedPrice
import com.example.jyghiretest.safeCollect
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding: FragmentProductDetailBinding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpImageButton()
        observeViewModel()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpImageButton() {
        binding.imageButtonFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun observeViewModel() {
        viewModel.state.safeCollect(viewLifecycleOwner) {
            when (it) {
                is ProductDetailState.Success -> {
                    updateUI(it.product)
                }
                ProductDetailState.Error -> {}
                ProductDetailState.Loading -> {}
            }
        }
    }

    private fun updateUI(product: Product) {
        binding.toolbar.title = product.name
        binding.textViewPrice.text = getString(R.string.label_price, product.formattedPrice())

        val drawableRes =
            if (product.isFavorite) R.drawable.ic_baseline_favorite_checked_24 else R.drawable.ic_baseline_favorite_unchecked_24
        val drawable = ContextCompat.getDrawable(binding.root.context, drawableRes)
        binding.imageButtonFavorite.setImageDrawable(drawable)
    }

}
