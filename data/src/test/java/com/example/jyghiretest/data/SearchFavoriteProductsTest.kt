package com.example.jyghiretest.data

import app.cash.turbine.test
import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.store.DataStoreSearchQueryStore
import com.example.jyghiretest.model.Product
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchFavoriteProductsTest {

    @Test
    fun invoke_given_query_then_save_and_returns() = runTest {
        val keyword = "키워드"

        val products = List(5) {
            Product("$it", "", "키워드$it", 100, false)
        }
        val mockStore: DataStoreSearchQueryStore = mockk()
        val mockRepository: FavoriteRepository = mockk()

        coEvery { mockRepository.searchFavoriteProducts(keyword) } returns flow { emit(products) }
        coEvery { mockStore.save(keyword) } returns Unit

        val searchFavoriteProducts = SearchFavoriteProducts(
            mockRepository, mockStore
        )

        val flow = searchFavoriteProducts.flow

        flow.test {
            searchFavoriteProducts(keyword)

            assert(awaitItem().size == 5)

            coVerify { mockStore.save(keyword) }
            confirmVerified(mockStore)
        }

    }

}

