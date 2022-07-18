package com.example.jyghiretest.product.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.example.jyghiretest.R
import com.example.jyghiretest.databinding.FragmentProductDetailBinding
import com.example.jyghiretest.model.Product
import com.example.jyghiretest.product.model.formattedPrice
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.viewmodel.compose.viewModel

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding: FragmentProductDetailBinding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.composeView.setContent {
            MdcTheme {
                val viewModel: ProductDetailViewModel = viewModel()
                val state by viewModel.state.collectAsState()
                ProductDetailScreen(
                    state = state,
                    onBackButtonClick = { findNavController().popBackStack() },
                    onFavoriteClick = { viewModel.toggleFavorite() }
                )
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}

@Composable
fun ProductDetailScreen(
    state: ProductDetailState,
    onBackButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    when (state) {
        is ProductDetailState.Success ->
            ProductDetailContent(
                state = state,
                onBackButtonClick = onBackButtonClick,
                onFavoriteClick = onFavoriteClick
            )
        else -> {}
    }

}

@Composable
fun ProductDetailContent(
    state: ProductDetailState.Success,
    onBackButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                title = { Text(text = state.product.name) },
                navigationIcon = {
                    IconButton(onClick = onBackButtonClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "back")
                    }
                }
            )
        }
    ) {
        Row(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(id = R.string.label_price, state.product.formattedPrice()),
                fontSize = 16.sp
            )
            IconButton(
                modifier = Modifier.padding(16.dp),
                onClick = onFavoriteClick
            ) {
                val resourceId =
                    if (state.product.isFavorite) R.drawable.ic_baseline_favorite_checked_24 else R.drawable.ic_baseline_favorite_unchecked_24
                Icon(
                    painter = painterResource(id = resourceId),
                    contentDescription = "favorite",
                    tint = Color.Unspecified
                )
            }
        }
    }
}


@Preview
@Composable
fun ProductDetailScreenPreview() {
    val state = ProductDetailState.Success(
        product = Product(
            "", "", "test", 1000, true
        )
    )
    ProductDetailScreen(state, {}, {})
}
