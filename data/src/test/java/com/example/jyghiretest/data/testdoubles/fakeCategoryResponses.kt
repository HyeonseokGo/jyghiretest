package com.example.jyghiretest.data.testdoubles

import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.utils.CategoryResponseToEntity

val fakeCategoryResponses = listOf(
    CategoryResponse("0", "카테고리1"),
    CategoryResponse("1", "카테고리2"),
    CategoryResponse("2", "카테고리3"),
    CategoryResponse("3", "카테고리4"),
    CategoryResponse("4", "카테고리5"),
    CategoryResponse("5", "카테고리6"),
    CategoryResponse("6", "카테고리7"),
    CategoryResponse("7", "카테고리8"),
    CategoryResponse("8", "카테고리9")
)


suspend fun List<CategoryResponse>.toEntities() =
    mutableListOf<CategoryEntity>().apply {
        addAll(fakeCategoryResponses.map {
            CategoryResponseToEntity().invoke(it)
        })
    }