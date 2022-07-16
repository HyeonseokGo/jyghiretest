package com.example.jyghiretest.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.jyghiretest.data.CategoryRepository
import com.example.jyghiretest.data.DefaultCategoryRepository
import com.example.jyghiretest.data.TestDispatcherRule
import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.JygDatabase
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.testdoubles.testDispatcherProvider
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryRepositoryTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var repository: CategoryRepository

    private lateinit var dao: CategoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context, JygDatabase::class.java
        ).build()
        dao = database.categoryDao()
        repository = DefaultCategoryRepository(
            dao, dispatcherRule.dispatcher.testDispatcherProvider()
        )
    }

    @Test
    fun test_observeCategories() = runTest {
        val entity1 = CategoryEntity("C0", "1")
        val entity2 = CategoryEntity("C1", "1")
        val entity3 = CategoryEntity("C2", "1")
        val entity4 = CategoryEntity("C3", "1")

        dao.insertAll(entity1, entity2, entity3, entity4)

        repository.observeCategories().test {
            assert(awaitItem().size == 4)
        }
    }

    @Test
    fun test_observeCategories_updated() = runTest {
        val entity1 = CategoryEntity("C0", "1")
        val entity2 = CategoryEntity("C1", "1")
        val entity3 = CategoryEntity("C2", "1")
        val entity4 = CategoryEntity("C3", "1")


        repository.observeCategories().test {
            assert(awaitItem().isEmpty())
            dao.insertAll(entity1, entity2, entity3, entity4)
            assert(awaitItem().size == 4)
        }
    }

}
