package com.example.jyghiretest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.example.jyghiretest.databinding.ActivityMainBinding
import com.example.jyghiretest.product.detail.PRODUCT_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.safeCollect(this) {
            it.navigateProductKey?.let {
                val bundle = Bundle().apply {
                    putString(PRODUCT_KEY, it)
                }
                findNavController(binding.navigationHostFragment.id).navigate(R.id.productDetailFragment, bundle)
                viewModel.navigated()
            }
        }
    }

}
