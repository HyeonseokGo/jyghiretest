package com.example.jyghiretest.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jyghiretest.WhileUiSubscribed
import com.example.jyghiretest.asConsumableFlow
import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.SearchFavoriteProducts
import com.example.jyghiretest.data.store.DataStoreSearchQueryStore
import com.example.jyghiretest.data.store.SearchQueryStore
import com.example.jyghiretest.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FavoriteProductsViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val searchFavoriteProducts: SearchFavoriteProducts,
    dataStoreSearchQueryStore: SearchQueryStore,
    @Named("SearchQueryDebounce") SEARCH_QUERY_DEBOUNCE: Long
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val lastSearchQuery = dataStoreSearchQueryStore.lastSearchQuery().asConsumableFlow()

    val state: StateFlow<FavoriteProductState> =
        searchFavoriteProducts.flow.map(::FavoriteProductState)
            .stateIn(
                viewModelScope,
                WhileUiSubscribed,
                FavoriteProductState.Empty
            )

    init {
        viewModelScope.launch {
            searchQuery.debounce(SEARCH_QUERY_DEBOUNCE)
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
