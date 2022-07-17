package com.example.jyghiretest.product.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.repository.ProductRepository
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductsByCategoryViewModel @Inject constructor(
    productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryKey = savedStateHandle.get<String>(CATEGORY_KEY) ?: ""

    val state: StateFlow<ProductsByCategoryState> =
        productRepository.observeProductsByCategory(categoryKey)
            .distinctUntilChanged()
            .map {
                ProductsByCategoryState(it)
            }
            .stateIn(
                viewModelScope,
                WhileUiSubscribed,
                ProductsByCategoryState.Empty
            )

    fun toggleFavorite(key: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(key)
        }
    }

}

data class ProductsByCategoryState(
    val products: List<Product>
) {

    companion object {
        val Empty: ProductsByCategoryState
            get() = ProductsByCategoryState(listOf())
    }

}
