package com.example.jyghiretest.data.model

import com.example.jyghiretest.data.database.entity.ProductEntity
import com.example.jyghiretest.model.Product

fun ProductEntity.toModel(): Product {
    return Product(
        key = key,
        name = name,
        categoryKey = categoryKey,
        price = price,
        liked = liked
    )
}
