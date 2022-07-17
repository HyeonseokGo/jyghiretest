package com.example.jyghiretest.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.jyghiretest.data.test.testdoubles.FakeFavoriteRepository
import com.example.jyghiretest.data.test.testdoubles.FakeProductDataSource
import com.example.jyghiretest.data.test.testdoubles.FakeProductRepository
import com.example.jyghiretest.model.Product
import com.example.jyghiretest.product.list.CATEGORY_KEY
import com.example.jyghiretest.product.list.ProductsByCategoryViewModel
import com.example.jyghiretest.utils.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductsByCategoryViewModelTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var dataSource: FakeProductDataSource
    private lateinit var productRepository: FakeProductRepository
    private lateinit var favoriteRepository: FakeFavoriteRepository
    private lateinit var viewModel: ProductsByCategoryViewModel

    @Before
    fun setUp() {
        dataSource = FakeProductDataSource()
        productRepository = FakeProductRepository(dataSource)
        favoriteRepository = FakeFavoriteRepository(dataSource)
        val savedStateHandle: SavedStateHandle = mockk()
        every { savedStateHandle.get<String>(CATEGORY_KEY) } returns "C0"
        viewModel = ProductsByCategoryViewModel(productRepository, favoriteRepository, savedStateHandle)
    }

    @Test
    fun state_when_initialized_then_returns_empty_list() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
        }
    }

    @Test
    fun state_when_observed_then_returns_products_by_category_list() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
            dataSource.setProducts(getProducts())
            awaitItem().run {
                assert(products.size == 3)
                assert(products.all { it.categoryKey == "C0" })
            }
        }
    }

    @Test
    fun state_when_toggleFavorite_called_then_updated() = runTest {
        viewModel.state.test {
            assert(awaitItem().products.isEmpty())
            dataSource.setProducts(getProducts())
            awaitItem().run {
                assert(products.size == 3)
                assert(products.all { it.categoryKey == "C0" })
            }
            val keyToChangeIsFavorite = viewModel.state.value.products.first().key
            viewModel.toggleFavorite(keyToChangeIsFavorite)
            awaitItem().run {
                assert(products.size == 3)
                assert(products.all { it.categoryKey == "C0" })
                assert(products.single { it.key == keyToChangeIsFavorite }.isFavorite)
            }
        }
    }


    private fun getProducts() = listOf(
        Product("0", "C0", "키워드1-1", 100, false),
        Product("1", "0", "키워드1-2", 100, false),
        Product("2", "C0", "키워드2-1", 100, false),
        Product("3", "0", "키워드2-2", 100, false),
        Product("4", "C0", "키워드3-1", 100, false),
        Product("5", "0", "키워드4-2", 100, false),
    )


}
