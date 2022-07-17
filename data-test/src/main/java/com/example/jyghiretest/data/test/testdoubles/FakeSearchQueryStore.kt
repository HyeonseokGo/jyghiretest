package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.data.store.SearchQueryStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSearchQueryStore: SearchQueryStore {

    val flow = MutableStateFlow("")

    override fun lastSearchQuery(): Flow<String> {
        return flow
    }

    override suspend fun save(keyword: String) {
        flow.value = keyword
    }

}

