package com.example.jyghiretest.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider() = DispatcherProvider(
        io = Dispatchers.IO,
        main =  Dispatchers.Main
    )

}

data class DispatcherProvider(
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher
)

