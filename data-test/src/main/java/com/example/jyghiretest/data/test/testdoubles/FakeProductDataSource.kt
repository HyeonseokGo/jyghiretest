package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.model.Product
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeProductDataSource {

    val flow: MutableSharedFlow<List<Product>> = MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    suspend fun setProducts(products: List<Product>) {
        flow.emit( products)
    }

}
