package com.example.jyghiretest.product.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.data.ProductRepository
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class ProductsByCategoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
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

}

data class ProductsByCategoryState(
    val products: List<Product>
) {

    companion object {
        val Empty: ProductsByCategoryState
            get() = ProductsByCategoryState(listOf())
    }

}
