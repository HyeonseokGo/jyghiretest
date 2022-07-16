package com.example.jyghiretest.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.asConsumableFlow
import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.SearchFavoriteProducts
import com.example.jyghiretest.data.store.SearchQueryStore
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteProductsViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val searchFavoriteProducts: SearchFavoriteProducts,
    private val searchQueryStore: SearchQueryStore,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val lastSearchQuery = searchQueryStore.lastSearchQuery().asConsumableFlow()

    val state: StateFlow<FavoriteProductState> =
        searchFavoriteProducts.flow.map(::FavoriteProductState)
            .stateIn(
                viewModelScope,
                WhileUiSubscribed,
                FavoriteProductState.Empty
            )

    init {
        viewModelScope.launch {
            searchQuery.debounce(200)
                .distinctUntilChanged()
                .onEach {
                    searchFavoriteProducts(it)
                }.collect()
        }
    }

    fun toggleFavorite(key: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(key)
        }
    }

    fun search(keyword: String) {
        searchQuery.value = keyword
    }

}

data class FavoriteProductState(
    val products: List<Product>,
) {

    companion object {
        val Empty: FavoriteProductState
            get() = FavoriteProductState(listOf())
    }

}
