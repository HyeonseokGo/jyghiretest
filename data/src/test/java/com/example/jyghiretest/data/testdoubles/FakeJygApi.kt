package com.example.jyghiretest.data.testdoubles

import com.example.jyghiretest.data.network.JygApi
import com.example.jyghiretest.data.network.model.ProductListResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response

object FakeJygApi : JygApi {
    override suspend fun getAppData(): Response<ProductListResponse> {
        val productListResponse = Gson().fromJson(fakeProductListJsonResponse, ProductListResponse::class.java)
        return Response.success(productListResponse)
    }
}


object FailingFakeJygApi: JygApi {
    override suspend fun getAppData(): Response<ProductListResponse> {
        return Response.error(400, ResponseBody.create(null, ""))
    }
}

