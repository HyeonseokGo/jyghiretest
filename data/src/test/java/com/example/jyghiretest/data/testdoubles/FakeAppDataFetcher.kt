package com.example.jyghiretest.data.testdoubles

import com.example.jyghiretest.data.network.AppDataFetcher
import com.example.jyghiretest.data.network.model.ProductListResponse

class FakeAppDataFetcher(
    private val response: ProductListResponse
): AppDataFetcher {
    override suspend fun fetch(): ProductListResponse {
        return response
    }
}
