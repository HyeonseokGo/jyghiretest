package com.example.jyghiretest.data.network

import com.example.jyghiretest.data.network.di.DispatcherProvider
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductListResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

interface AppDataFetcher {
    suspend fun fetch(): ProductListResponse
}


class RetrofitAppDataFetcher @Inject constructor(
    private val api: JygApi,
    private val dispatcherProvider: DispatcherProvider
) : AppDataFetcher {

    override suspend fun fetch(): ProductListResponse = withContext(dispatcherProvider.io) {
        val response = api.getAppData()
        response.body() ?: throw HttpException(response)
    }

}
