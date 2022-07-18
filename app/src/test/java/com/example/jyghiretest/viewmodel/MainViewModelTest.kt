package com.example.jyghiretest.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.jyghiretest.MainViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @Test
    fun state_when_initialized_then_returns_empty() = runTest {
        viewModel.state.test {
            assert(awaitItem().navigateProductKey == null)
        }
    }

    @Test
    fun state_when_navigateToProductDetail_called_then_returns_key() = runTest {
        val key = "navigate_key"
        viewModel.state.test {
            assert(awaitItem().navigateProductKey == null)
            viewModel.navigateToProductDetail(key)
            assert(awaitItem().navigateProductKey == key)
        }
    }

    @Test
    fun state_when_navigated_called_then_empty() = runTest {
        val key = "navigate_key"
        viewModel.state.test {
            assert(awaitItem().navigateProductKey == null)
            viewModel.navigateToProductDetail(key)
            assert(awaitItem().navigateProductKey == key)
            viewModel.navigated()
            assert(awaitItem().navigateProductKey == null)
        }
    }


}
