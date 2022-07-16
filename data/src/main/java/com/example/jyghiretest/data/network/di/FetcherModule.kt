package com.example.jyghiretest.data.network.di

import com.example.jyghiretest.data.network.AppDataFetcher
import com.example.jyghiretest.data.network.RetrofitAppDataFetcher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FetcherModule {

    @Binds
    @Singleton
    fun bindAppDataFetcher(
        appDataFetcher: RetrofitAppDataFetcher
    ): AppDataFetcher

}
