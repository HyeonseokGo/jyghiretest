package com.example.jyghiretest.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jyghiretest.databinding.FragmentFavoriteProductsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteProductsFragment : Fragment() {

    private var _binding: FragmentFavoriteProductsBinding? = null
    private val binding: FragmentFavoriteProductsBinding get() = _binding!!

    private val viewModel: FavoriteProductsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteProductsBinding.inflate(inflater)
        return binding.root
    }

    companion object {

        fun newInstance() = FavoriteProductsFragment()

    }

}
