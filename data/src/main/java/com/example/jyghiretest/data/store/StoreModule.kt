package com.example.jyghiretest.data.store

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface StoreModule {

    @Binds
    @Singleton
    fun bindSearchQueryStore(
        searchQueryStore: DataStoreSearchQueryStore
    ): SearchQueryStore

}
