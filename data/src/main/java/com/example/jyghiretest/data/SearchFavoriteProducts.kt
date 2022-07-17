package com.example.jyghiretest.data

import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.data.store.SearchQueryStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
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
        .distinctUntilChanged()
        .onEach(searchQueryStore::save)
        .flatMapLatest(repository::searchFavoriteProducts)
        .distinctUntilChanged()

    operator fun invoke(keyword: String) {
        triggerFlow.tryEmit(keyword)
    }

}
