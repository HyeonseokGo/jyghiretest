package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.data.repository.ProductRepository
import com.example.jyghiretest.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeProductRepository(
    private val dataSource: FakeProductDataSource
) : ProductRepository {


    override fun observeProducts(): Flow<List<Product>> {
        return dataSource.flow
    }

    override fun observeProductsByCategory(categoryKey: String): Flow<List<Product>> {
        return  dataSource.flow.map { list ->
            list.filter {
                it.categoryKey == categoryKey
            }
        }
    }

    override fun observeProduct(key: String): Flow<Product?> {
        return  dataSource.flow.map { list ->
            list.singleOrNull {
                it.key == key
            }
        }
    }

}
