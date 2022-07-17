package com.example.jyghiretest.data.store

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchQueryStoreTest {

    private lateinit var store: SearchQueryStore

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        store = SearchQueryStore(
            context = context
        )
    }

    @Test
    fun lastSearchQuery_when_initialized_then_returns_emptyString() = runTest {
        store.lastSearchQuery().test {
            assert(awaitItem().isEmpty())
        }
    }

    @Test
    fun lastSearchQuery_when_save_called_then_returns_savedKeyword() = runTest {
        store.lastSearchQuery().test {
            assert(awaitItem().isEmpty())
            store.save("test_query")
            assert(awaitItem() == "test_query")
        }
    }

}
