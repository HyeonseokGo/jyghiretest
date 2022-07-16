package com.example.jyghiretest.data

import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.store.SearchQueryStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class SearchFavoriteProducts @Inject constructor(
    private val repository: FavoriteRepository,
    private val searchQueryStore: SearchQueryStore,
) {

    private val triggerFlow = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow = triggerFlow
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            searchQueryStore.save(it)
            repository.searchFavoriteProducts(it)
        }.distinctUntilChanged()

    operator fun invoke(keyword: String) {
        triggerFlow.tryEmit(keyword)
    }

}
