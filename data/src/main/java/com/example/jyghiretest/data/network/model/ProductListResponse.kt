package com.example.jyghiretest.data.network.model

data class ProductListResponse(
    val categories: List<CategoryResponse>,
    val productions: List<ProductResponse>
)
