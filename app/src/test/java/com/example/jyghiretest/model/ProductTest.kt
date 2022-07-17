package com.example.jyghiretest.model

import com.example.jyghiretest.product.model.formattedPrice
import org.junit.Test

class ProductTest {

    @Test
    fun test_formattedPrice() {
        val price = 12000
        val expected = "12,000"
        val product = Product("", "", "", price, false)
        assert(product.formattedPrice() == expected)
    }

}
