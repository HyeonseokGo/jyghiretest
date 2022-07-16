package com.example.jyghiretest.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.JygDatabase
import com.example.jyghiretest.data.database.entity.CategoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryDaoTest {

    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context, JygDatabase::class.java
        ).build()
        categoryDao = database.categoryDao()
    }

    @Test
    fun test_insert() = runTest {
        val entity = CategoryEntity(
            key = "C0", name = ""
        )
        categoryDao.insert(entity)
        assert(categoryDao.get("C0") != null)
    }

    @Test
    fun test_update() = runTest {
        val entity = CategoryEntity(
            key = "C0", name = ""
        )
        categoryDao.insert(entity)
        assert(categoryDao.get("C0") != null)

        categoryDao.update(entity.copy(name = "changed"))
        assert(categoryDao.get("C0")!!.name == "changed")
    }

    @Test
    fun test_delete() = runTest {
        val entity = CategoryEntity(
            key = "C0", name = ""
        )
        categoryDao.insert(entity)
        assert(categoryDao.get("C0") != null)

        categoryDao.delete(entity)
        assert(categoryDao.get("C0") == null)
    }

    @Test
    fun test_observeCategories() = runTest {
        val entity1 = CategoryEntity(
            key = "C0", name = ""
        )
        val entity2 = CategoryEntity(
            key = "C1", name = ""
        )
        categoryDao.insertAll(entity1, entity2)

        categoryDao.getAll().test {
            assert(awaitItem().size == 2)
        }

    }

}
