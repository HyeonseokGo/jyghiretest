package com.example.jyghiretest.data

import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.di.CategoryDatabaseSyncer
import com.example.jyghiretest.data.di.ProductDatabaseSyncer
import com.example.jyghiretest.data.network.AppDataFetcher
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import com.example.jyghiretest.data.utils.Syncable
import javax.inject.Inject

interface AppDataRepository

class DefaultAppDataRepository @Inject constructor(
    private val categoryDatabaseSyncer: CategoryDatabaseSyncer,
    private val productDatabaseSyncer: ProductDatabaseSyncer,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val appDataFetcher: AppDataFetcher
) : AppDataRepository, Syncable {

    override suspend fun sync() {
        kotlin.runCatching {
            appDataFetcher.fetch()
        }.onSuccess {
            val (categories, products) = it
            syncCategory(categories)
            syncProduct(products)
        }.onFailure {

        }
    }

    private suspend fun syncCategory(newItems: List<CategoryResponse>) {
        val oldItems = categoryDao.getAllImmediately()
        categoryDatabaseSyncer.run(
            newItems = newItems,
            oldItems = oldItems
        )
    }

    private suspend fun syncProduct(newItems: List<ProductResponse>) {
        val oldItems = productDao.getAllImmediately()
        productDatabaseSyncer.run(
            newItems = newItems,
            oldItems = oldItems
        )
    }

}
