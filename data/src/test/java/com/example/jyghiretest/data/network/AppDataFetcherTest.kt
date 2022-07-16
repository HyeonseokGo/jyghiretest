package com.example.jyghiretest.data.network

import com.example.jyghiretest.data.testdoubles.FailingFakeJygApi
import com.example.jyghiretest.data.testdoubles.FakeJygApi
import com.example.jyghiretest.data.testdoubles.testDispatcherProvider
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException


class AppDataFetcherTest {

    @Test
    fun test_returns_ProductListResponse() = runTest {
        val appDataFetcher = RetrofitAppDataFetcher(FakeJygApi, testDispatcherProvider())
        appDataFetcher.fetch().also { (categories, productions) ->
            assert(categories.isNotEmpty())
            assert(productions.isNotEmpty())
        }
    }

    @Test(expected = HttpException::class)
    fun test_throws_exception() = runTest {
        val appDataFetcher = RetrofitAppDataFetcher(FailingFakeJygApi, testDispatcherProvider())
        appDataFetcher.fetch()
    }

}
