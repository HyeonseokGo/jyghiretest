package com.example.jyghiretest.data.utils

import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import javax.inject.Inject

class CategoryEntityToKey @Inject constructor(): Mapper<CategoryEntity, String> {
    override suspend fun invoke(from: CategoryEntity): String {
        return from.key
    }
}


class CategoryResponseToKey @Inject constructor(): Mapper<CategoryResponse, String> {
    override suspend fun invoke(from: CategoryResponse): String {
        return from.key
    }
}


class CategoryResponseToEntity @Inject constructor(): Mapper<CategoryResponse, CategoryEntity> {
    override suspend fun invoke(from: CategoryResponse): CategoryEntity {
        return CategoryEntity(
            key = from.key,
            name = from.name
        )
    }
}


class ProductEntityToKey @Inject constructor(): Mapper<ProductEntity, String> {
    override suspend fun invoke(from: ProductEntity): String {
        return from.key
    }
}


class ProductResponseToKey @Inject constructor(): Mapper<ProductResponse, String> {
    override suspend fun invoke(from: ProductResponse): String {
        return from.key
    }
}


class ProductResponseToEntity @Inject constructor(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
): Mapper<ProductResponse, ProductEntity> {
    override suspend fun invoke(from: ProductResponse): ProductEntity {
        val productEntity = productDao.get(from.key)
        val liked = productEntity?.liked ?: false

        return ProductEntity(
            key = from.key,
            categoryKey = from.categoryKey,
            name = from.name,
            price = from.price,
            liked = liked
        )
    }
}

