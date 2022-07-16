package com.example.jyghiretest.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.jyghiretest.data.DefaultProductRepository
import com.example.jyghiretest.data.ProductRepository
import com.example.jyghiretest.data.TestDispatcherRule
import com.example.jyghiretest.data.database.JygDatabase
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.database.entity.ProductEntity
import com.example.jyghiretest.data.testdoubles.testDispatcherProvider
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProductRepositoryTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var productDao: ProductDao

    private lateinit var repository: ProductRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context, JygDatabase::class.java
        ).build()
        productDao = database.productDao()
        repository = DefaultProductRepository(
            dao = productDao,
            dispatcherProvider = dispatcherRule.dispatcher.testDispatcherProvider()
        )
    }

    @Test
    fun test_observeProductsByCategory() = runTest {
        val categoryKey = "testKey"
        val entities = listOf(
            ProductEntity(
                key = "0", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "1", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "2", categoryKey = "not", name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "3", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            )
        )
        productDao.insertAll(*entities.toTypedArray())

        repository.observeProductsByCategory(categoryKey).test {
            val list = awaitItem()
            assert(list.size == 3)
        }
    }

    @Test
    fun test_observeProductsByCategory_updated() = runTest {
        val categoryKey = "testKey"
        val entities = listOf(
            ProductEntity(
                key = "0", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "1", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "2", categoryKey = "not", name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "3", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            )
        )
        val entityToInsert = ProductEntity(
            key = "5", categoryKey = categoryKey, name = "0", price = 1000, liked = true

        )
        productDao.insertAll(*entities.toTypedArray())

        repository.observeProductsByCategory(categoryKey).test {
            assert(awaitItem().size == 3)
            productDao.insert(entityToInsert)
            assert(awaitItem().size == 4)
        }
    }

    @Test
    fun test_observeProducts() = runTest {
        val categoryKey = "testKey"
        val entities = listOf(
            ProductEntity(
                key = "0", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "1", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "2", categoryKey = "not", name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "3", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            )
        )
        productDao.insertAll(*entities.toTypedArray())

        repository.observeProducts().test {
            val list = awaitItem()
            assert(list.size == 4)
        }
    }

    @Test
    fun test_observeProducts_updated() = runTest {
        val categoryKey = "testKey"
        val entities = listOf(
            ProductEntity(
                key = "0", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "1", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "2", categoryKey = "not", name = "0", price = 1000, liked = true
            ),
            ProductEntity(
                key = "3", categoryKey = categoryKey, name = "0", price = 1000, liked = true
            )
        )

        repository.observeProducts().test {
            assert(awaitItem().isEmpty())
            productDao.insertAll(*entities.toTypedArray())
            assert(awaitItem().size == 4)
        }
    }

}
