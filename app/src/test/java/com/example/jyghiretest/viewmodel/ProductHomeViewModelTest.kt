package com.example.jyghiretest.viewmodel

import app.cash.turbine.test
import com.example.jyghiretest.data.test.testdoubles.FakeCategoryRepository
import com.example.jyghiretest.model.Category
import com.example.jyghiretest.product.home.ProductHomeViewModel
import com.example.jyghiretest.utils.TestDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductHomeViewModelTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: FakeCategoryRepository
    private lateinit var viewModel: ProductHomeViewModel

    @Before
    fun setUp() {
        repository = FakeCategoryRepository()
        viewModel = ProductHomeViewModel(repository)
    }

    @Test
    fun state_when_viewModel_initialized_then_returns_categories() = runTest {
        viewModel.categoryRepository.observeCategories().test {
            assert(awaitItem().isEmpty())
            repository.setCategories(
                listOf(Category("0", "C0"), Category("1", "C1"), Category("2", "C2"))
            )
            assert(awaitItem().isNotEmpty())
        }

    }

}
