package com.example.jyghiretest.product.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.jyghiretest.databinding.FragmentProductHomeBinding

class ProductHomeFragment : Fragment() {

    private var _binding: FragmentProductHomeBinding? = null
    private val binding: FragmentProductHomeBinding get() = _binding!!

    private val viewModel: ProductHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = ProductHomeFragment()

    }

}