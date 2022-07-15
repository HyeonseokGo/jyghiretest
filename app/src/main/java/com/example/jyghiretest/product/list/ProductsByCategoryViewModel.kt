package com.example.jyghiretest.product.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private val SAMPLE_PRODUCTS = listOf(
    Product(
        "0", "c0", "삼겹살", 10000, false
    ),
    Product(
        "1", "c0", "목살", 9000, false
    ),
    Product(
        "3", "c0", "등심", 20000, true
    ),
    Product(
        "4", "c0", "안심", 18000, true
    )
)

@HiltViewModel
class ProductsByCategoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryKey = savedStateHandle.get<String>(CATEGORY_KEY)

    val state: StateFlow<ProductsByCategoryState> = flow {
        emit(ProductsByCategoryState(SAMPLE_PRODUCTS))
    }.stateIn(
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
