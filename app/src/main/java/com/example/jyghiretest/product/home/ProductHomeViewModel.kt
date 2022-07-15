package com.example.jyghiretest.product.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.data.CategoryRepository
import com.example.jyghiretest.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ProductHomeViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {

    val state: StateFlow<ProductHomeState> = categoryRepository.observeCategories()
        .map {
            ProductHomeState(it)
        }
        .stateIn(viewModelScope, WhileUiSubscribed, ProductHomeState.Empty)

}

data class ProductHomeState(
    val categories: List<Category>
) {

    companion object {
        val Empty: ProductHomeState
            get() = ProductHomeState(listOf())
    }

}
