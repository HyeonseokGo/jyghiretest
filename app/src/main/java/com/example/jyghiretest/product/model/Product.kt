package com.example.jyghiretest.product.model

import com.example.jyghiretest.model.Product
import java.text.DecimalFormat

fun Product.formattedPrice(): String {
    return DecimalFormat("#,###").format(price)
}