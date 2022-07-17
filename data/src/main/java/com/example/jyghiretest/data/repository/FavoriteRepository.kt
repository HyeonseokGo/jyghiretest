package com.example.jyghiretest.data.repository

import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.model.toModel
import com.example.jyghiretest.data.network.di.DispatcherProvider
import com.example.jyghiretest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FavoriteRepository {
    suspend fun toggleFavorite(key: String)
    fun searchFavoriteProducts(keyword: String): Flow<List<Product>>
}


class DefaultFavoriteRepository @Inject constructor(
    private val productDao: ProductDao,
    private val dispatcherProvider: DispatcherProvider
) : FavoriteRepository {

    override suspend fun toggleFavorite(key: String) {
        val productEntity = productDao.getImmediately(key) ?: return
        val toggled = !productEntity.isFavorite
        productDao.update(productEntity.copy(isFavorite = toggled))
    }

    override fun searchFavoriteProducts(keyword: String) =
        productDao.searchFavoriteProducts(keyword).map {
            it.map { entity ->
                entity.toModel()
            }
        }.flowOn(dispatcherProvider.io)

}

