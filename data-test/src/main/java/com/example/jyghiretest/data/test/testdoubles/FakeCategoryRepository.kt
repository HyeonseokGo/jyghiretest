package com.example.jyghiretest.data.test.testdoubles

import com.example.jyghiretest.data.repository.CategoryRepository
import com.example.jyghiretest.model.Category
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCategoryRepository : CategoryRepository {

    private val categoriesFlow = MutableSharedFlow<List<Category>>(
        replay = 1, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun observeCategories(): Flow<List<Category>> {
        return categoriesFlow.asSharedFlow()
    }

    /**
     * For Testing
     */
    suspend fun setCategories(categories: List<Category>) {
        categoriesFlow.emit(categories)
    }

}
