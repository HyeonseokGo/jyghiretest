package com.example.jyghiretest.product.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.model.Category
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

private val SAMPLE_CATEGORIES = listOf(
    Category("0", "소"), Category("1", "돼지"),
    Category("2", "닭"), Category("3", "수산"),
    Category("4", "우유"), Category("5", "달걀"),
    Category("6", "이유식"), Category("7", "밀키트")
)

class ProductHomeViewModel : ViewModel() {

    val state: StateFlow<ProductHomeState> = flow {
        emit(
            ProductHomeState(SAMPLE_CATEGORIES)
        )
    }.stateIn(viewModelScope, WhileUiSubscribed, ProductHomeState.Empty)

}

data class ProductHomeState(
    val categories: List<Category>
) {

    companion object {
        val Empty: ProductHomeState
            get() = ProductHomeState(listOf())
    }

}
