package com.example.jyghiretest.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.jyghiretest.data.SearchFavoriteProducts
import com.example.jyghiretest.data.test.testdoubles.FakeFavoriteRepository
import com.example.jyghiretest.data.test.testdoubles.FakeProductDataSource
import com.example.jyghiretest.data.test.testdoubles.FakeSearchQueryStore
import com.example.jyghiretest.favorite.FavoriteProductsViewModel
import com.example.jyghiretest.model.Product
import com.example.jyghiretest.utils.TestDispatcherRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteProductsViewModelTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var dataSource: FakeProductDataSource
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var searchFavoriteProducts: SearchFavoriteProducts
    private lateinit var searchQueryStore: FakeSearchQueryStore
    private lateinit var viewModel: FavoriteProductsViewModel

    @Before
    fun setUp() {
        dataSource = FakeProductDataSource()
        favoriteRepository = FakeFavoriteRepository(dataSource)
        searchQueryStore = FakeSearchQueryStore()
        searchFavoriteProducts = SearchFavoriteProducts(favoriteRepository, searchQueryStore)
        viewModel = FavoriteProductsViewModel(
            favoriteRepository, searchFavoriteProducts, searchQueryStore, 0
        )
    }

    @Test
    fun state_when_initialized_then_returns_empty_list() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
        }
    }

    @Test
    fun state_when_search_called_then_returns_search_result_and_query_saved() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
            dataSource.setProducts(getProducts())
            awaitItem()
            viewModel.search("키워드1")
            assert(awaitItem().products.size == 2)
            assert(searchQueryStore.flow.value == "키워드1")
        }
    }

    @Test
    fun state_when_search_called_two_times_then_returns_last_search_result() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
            dataSource.setProducts(getProducts())
            awaitItem()
            viewModel.search("키워드1")
            assert(awaitItem().products.size == 2)
            assert(searchQueryStore.flow.value == "키워드1")
            viewModel.search("키워드4")
            assert(awaitItem().products.size == 1)
        }
    }

    @Test
    fun lastSearchQuery_when_initialized_then_returns_query() = runTest {
        val query = "저장된 키워드"
        searchQueryStore.save(query)

        viewModel.lastSearchQuery.test {
            assert(awaitItem() == query)
        }
    }

    @Test
    fun state_when_search_called_and_toggleFavorite_then_search_result_updated() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
            dataSource.setProducts(getProducts())
            awaitItem()
            viewModel.search("키워드1")
            assert(awaitItem().products.size == 2)

            viewModel.toggleFavorite(viewModel.state.value.products.first().key)
            assert(awaitItem().products.size == 1)
        }
    }

    private fun getProducts() = listOf(
        Product("0", "0", "키워드1-1", 100, true),
        Product("1", "0", "키워드1-2", 100, true),
        Product("2", "0", "키워드2-1", 100, true),
        Product("3", "0", "키워드2-2", 100, true),
        Product("4", "0", "키워드3-1", 100, true),
        Product("5", "0", "키워드4-2", 100, true),
    )

}
