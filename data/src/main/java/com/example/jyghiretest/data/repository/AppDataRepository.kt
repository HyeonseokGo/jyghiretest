package com.example.jyghiretest.data.repository

import android.util.Log
import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.di.CategoryDatabaseSyncer
import com.example.jyghiretest.data.di.ProductDatabaseSyncer
import com.example.jyghiretest.data.network.AppDataFetcher
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import com.example.jyghiretest.data.utils.Syncable
import javax.inject.Inject

interface AppDataRepository: Syncable

class DefaultAppDataRepository @Inject constructor(
    private val categoryDatabaseSyncer: CategoryDatabaseSyncer,
    private val productDatabaseSyncer: ProductDatabaseSyncer,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val appDataFetcher: AppDataFetcher
) : AppDataRepository {

    override suspend fun sync(): Result<Unit> {
        return kotlin.runCatching {
            Log.e(this::class.simpleName, "fetch()")
            appDataFetcher.fetch()
        }.onSuccess {
            val (categories, products) = it
            syncCategory(categories)
            syncProduct(products)
        }.map {}
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
