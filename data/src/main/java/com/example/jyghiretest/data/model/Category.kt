package com.example.jyghiretest.data.model

import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.model.Category

fun CategoryEntity.toModel(): Category {
    return Category(
        key = key,
        name = name
    )
}
