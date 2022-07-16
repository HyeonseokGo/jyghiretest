package com.example.jyghiretest.data.di

import com.example.jyghiretest.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindProductRepository(
        productRepository: DefaultProductRepository
    ): ProductRepository


    @Binds
    @Singleton
    fun bindCategoryRepository(
        categoryRepository: DefaultCategoryRepository
    ): CategoryRepository


    @Binds
    @Singleton
    fun bindAppDataRepository(
        appDataRepository: DefaultAppDataRepository
    ): AppDataRepository

    @Binds
    @Singleton
    fun bindFavoriteRepository(
        favoriteRepository: DefaultFavoriteRepository
    ): FavoriteRepository

}
