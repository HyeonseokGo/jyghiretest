package com.example.jyghiretest.data.network

import com.example.jyghiretest.data.network.model.ProductListResponse
import retrofit2.Response
import retrofit2.http.GET

interface JygApi {
    @GET
    suspend fun getAppData(): Response<ProductListResponse>
}
