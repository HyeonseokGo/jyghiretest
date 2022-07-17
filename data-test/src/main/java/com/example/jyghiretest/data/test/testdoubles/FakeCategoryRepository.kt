package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.data.repository.CategoryRepository
import com.example.jyghiretest.model.Category
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCategoryRepository : CategoryRepository {

    private val categoriesFlow: MutableSharedFlow<List<Category>> = MutableSharedFlow()

    override fun observeCategories(): Flow<List<Category>> = categoriesFlow

    /**
     * For Testing
     */
    suspend fun setCategories(categories: List<Category>) = categoriesFlow.emit(categories)


}
