package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.data.repository.FavoriteRepository
import com.example.jyghiretest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FakeFavoriteRepository(
    private val dataSource: FakeProductDataSource
) : FavoriteRepository {

    override suspend fun toggleFavorite(key: String) {
        val products = dataSource.flow.first().toList()
        products.map {
            if (it.key == key) it.copy(isFavorite = !it.isFavorite)
            else it
        }.also {
            dataSource.setProducts(it)
        }
    }

    override fun searchFavoriteProducts(keyword: String): Flow<List<Product>> {
        return dataSource.flow.map { list ->
            list.filter { it.isFavorite }.filter { it.name.contains(keyword) }
        }
    }

}
