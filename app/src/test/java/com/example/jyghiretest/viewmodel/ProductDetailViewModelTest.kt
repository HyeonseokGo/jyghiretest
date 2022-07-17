package com.example.jyghiretest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.jyghiretest.data.test.testdoubles.FakeFavoriteRepository
import com.example.jyghiretest.data.test.testdoubles.FakeProductDataSource
import com.example.jyghiretest.data.test.testdoubles.FakeProductRepository
import com.example.jyghiretest.model.Product
import com.example.jyghiretest.product.detail.PRODUCT_KEY
import com.example.jyghiretest.product.detail.ProductDetailState
import com.example.jyghiretest.product.detail.ProductDetailViewModel
import com.example.jyghiretest.utils.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ProductDetailViewModelTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var dataSource: FakeProductDataSource
    private lateinit var productRepository: FakeProductRepository
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var viewModel: ProductDetailViewModel

    @Before
    fun setUp() {
        val mockSavedStateHandle: SavedStateHandle = mockk()
        every { mockSavedStateHandle.get<String>(PRODUCT_KEY) } returns "0"
        dataSource = FakeProductDataSource()
        productRepository = FakeProductRepository(dataSource)
        favoriteRepository = FakeFavoriteRepository(dataSource)
        viewModel = ProductDetailViewModel(productRepository, favoriteRepository, mockSavedStateHandle)
    }

    @Test
    fun state_when_initialized_then_returns_Product() = runTest {
        viewModel.state.test {
            val stateLoading = awaitItem()
            assert(stateLoading == ProductDetailState.Loading)
            dataSource.setProducts(getProductsContainsGivenKey())
            assert((awaitItem() as ProductDetailState.Success).product.key == "0")
        }
    }

    @Test
    fun state_when_initialized_then_returns_Error() = runTest {
        viewModel.state.test {
            assert(awaitItem() == ProductDetailState.Loading)
            dataSource.setProducts(getProductsWithoutGivenKey())
            assert(awaitItem() == ProductDetailState.Error)
        }
    }

    @Test
    fun state_when_toggleFavorite_then_isFavorite_true() = runTest {
        viewModel.state.test {
            assert(awaitItem() == ProductDetailState.Loading)
            dataSource.setProducts(getProductsContainsGivenKey())
            assert(!(awaitItem() as ProductDetailState.Success).product.isFavorite)
            viewModel.toggleFavorite()
            assert((awaitItem() as ProductDetailState.Success).product.isFavorite)
        }
    }

    private fun getProductsContainsGivenKey(): List<Product> {
        return List(5) {
            Product("$it", "$it", "$it", it, false)
        }
    }

    private fun getProductsWithoutGivenKey(): List<Product> {
        return (1..5).map {
            Product("$it", "$it", "$it", it, false)
        }
    }

}
