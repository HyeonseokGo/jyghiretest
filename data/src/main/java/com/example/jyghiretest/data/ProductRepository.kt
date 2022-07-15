package com.example.jyghiretest.data

import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.model.toModel
import com.example.jyghiretest.data.network.di.DispatcherProvider
import com.example.jyghiretest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProductRepository {
    suspend fun observeProducts(): Flow<List<Product>>
}


class DefaultProductRepository @Inject constructor(
    private val dao: ProductDao,
    private val dispatcherProvider: DispatcherProvider
) : ProductRepository {

    override suspend fun observeProducts(): Flow<List<Product>> {
        return dao.getAll().map {
            it.map { entity ->
                entity.toModel()
            }
        }.flowOn(dispatcherProvider.io)
    }

}
