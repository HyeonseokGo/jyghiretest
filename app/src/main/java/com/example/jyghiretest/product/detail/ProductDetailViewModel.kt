package com.example.jyghiretest.product.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.repository.ProductRepository
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PRODUCT_KEY = "productKey"

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productKey = savedStateHandle.get<String>(PRODUCT_KEY) ?: ""

    val state: StateFlow<ProductDetailState> =
        productRepository.observeProduct(productKey).map {
            if (it != null) {
                ProductDetailState.Success(it)
            } else {
                ProductDetailState.Error
            }
        }.stateIn(
            viewModelScope, WhileUiSubscribed, ProductDetailState.Loading
        )

    fun toggleFavorite() {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(productKey)
        }
    }

}

sealed interface ProductDetailState {
    data class Success(
        val product: Product
    ) : ProductDetailState

    object Error: ProductDetailState

    object Loading : ProductDetailState
}