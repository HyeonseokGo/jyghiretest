package com.example.jyghiretest.data.di

import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import com.example.jyghiretest.data.utils.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


typealias CategoryDatabaseSyncer = DatabaseSyncer<CategoryResponse, CategoryEntity, String>
typealias ProductDatabaseSyncer = DatabaseSyncer<ProductResponse, ProductEntity, String>


@Module
@InstallIn(SingletonComponent::class)
object DatabaseSyncerModule {

    @Provides
    @Singleton
    fun provideCategoryDatabaseSyncer(
        categoryDao: CategoryDao,
        categoryEntityToKey: CategoryEntityToKey,
        categoryResponseToKey: CategoryResponseToKey,
        categoryResponseToEntity: CategoryResponseToEntity
    ): CategoryDatabaseSyncer {
        return DatabaseSyncer(
            insert = categoryDao::insert,
            update = categoryDao::update,
            delete = categoryDao::delete,
            localToKey = categoryEntityToKey,
            networkToKey = categoryResponseToKey,
            networkToLocal = categoryResponseToEntity
        )
    }


    @Provides
    @Singleton
    fun provideProductDatabaseSyncer(
        productDao: ProductDao,
        productEntityToKey: ProductEntityToKey,
        productResponseToKey: ProductResponseToKey,
        productResponseToEntity: ProductResponseToEntity
    ): ProductDatabaseSyncer {
        return DatabaseSyncer(
            insert = productDao::insert,
            update = productDao::update,
            delete = productDao::delete,
            localToKey = productEntityToKey,
            networkToKey = productResponseToKey,
            networkToLocal = productResponseToEntity
        )
    }

}
