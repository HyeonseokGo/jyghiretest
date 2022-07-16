package com.example.jyghiretest.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.jyghiretest.data.database.JygDatabase
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.database.entity.ProductEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProductDaoTest {

    private lateinit var productDao: ProductDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context, JygDatabase::class.java
        ).build()
        productDao = database.productDao()
    }

    @Test
    fun test_insert() = runTest {
        productDao.insert(
            ProductEntity("0", "", "", 0, false)
        )
        assert(productDao.get("0") != null)
    }

    @Test
    fun test_update() = runTest {
        val entity =            ProductEntity("0", "", "", 0, false)

        productDao.insert(entity)
        assert(productDao.get("0") != null)

        productDao.update(entity.copy(name = "changed", liked = true))
        productDao.get("0").also {
            assert(it != null)
            assert(it!!.name == "changed")
            assert(it.liked)
        }
    }

    @Test
    fun test_delete() = runTest {
        val entity = ProductEntity("0", "", "", 0, false)
        productDao.insert(entity)
        assert(productDao.get("0") != null)

        productDao.delete(entity)
        assert(productDao.get("0") == null)
    }

    @Test
    fun test_getByCategory() = runTest {
        val categoryKey = "C0"
        val entities = listOf(
            ProductEntity(
                key = "0",
                categoryKey = categoryKey,
                name = "0",
                price = 1000,
                liked = true
            ),
            ProductEntity(
                key = "1",
                categoryKey = "C1",
                name = "0",
                price = 1000,
                liked = true
            ),
            ProductEntity(
                key = "2",
                categoryKey = categoryKey,
                name = "0",
                price = 1000,
                liked = true
            )
        )

        productDao.insertAll(*entities.toTypedArray())

        productDao.getByCategory(categoryKey).test {
            val list = awaitItem()
            assert(list.size == 2)
        }
    }

}
