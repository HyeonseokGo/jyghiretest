package com.example.jyghiretest.data.repository

import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.model.toModel
import com.example.jyghiretest.data.network.di.DispatcherProvider
import com.example.jyghiretest.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CategoryRepository {
    fun observeCategories(): Flow<List<Category>>
}


class DefaultCategoryRepository @Inject constructor(
    private val dao: CategoryDao,
    private val dispatcherProvider: DispatcherProvider
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> {
        return dao.getAll().map {
            it.map { entity ->
                entity.toModel()
            }
        }.flowOn(dispatcherProvider.io)
    }

}
