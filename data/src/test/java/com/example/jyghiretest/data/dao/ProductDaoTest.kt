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
        val entity = ProductEntity("0", "", "", 0, false)

        productDao.insert(entity)
        assert(productDao.get("0") != null)

        productDao.update(entity.copy(name = "changed", isFavorite = true))
        productDao.get("0").also {
            assert(it != null)
            assert(it!!.name == "changed")
            assert(it.isFavorite)
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
                isFavorite = true
            ),
            ProductEntity(
                key = "1",
                categoryKey = "C1",
                name = "0",
                price = 1000,
                isFavorite = true
            ),
            ProductEntity(
                key = "2",
                categoryKey = categoryKey,
                name = "0",
                price = 1000,
                isFavorite = true
            )
        )

        productDao.insertAll(*entities.toTypedArray())

        productDao.getByCategory(categoryKey).test {
            val list = awaitItem()
            assert(list.size == 2)
        }
    }

    @Test
    fun test_searchFavoriteProducts_returns_4_with_empty_keyword() = runTest {
        val entities = getSearchEntities()
        productDao.insertAll(*entities.toTypedArray())

        productDao.searchFavoriteProducts("").test {
            assert(awaitItem().size == 4)
        }
    }

    @Test
    fun test_searchFavoriteProducts_returns_3_with_keyword_키워드() = runTest {
        val entities = getSearchEntities()
        productDao.insertAll(*entities.toTypedArray())

        productDao.searchFavoriteProducts("키워드").test {
            assert(awaitItem().size == 3)
        }
    }

    @Test
    fun test_searchFavoriteProducts_returns_0_with_keyword_키워드3() = runTest {
        val entities = getSearchEntities()
        productDao.insertAll(*entities.toTypedArray())

        productDao.searchFavoriteProducts("키워드3").test {
            val list = awaitItem()
            assert(list.isEmpty())
        }
    }

    private fun getSearchEntities() = listOf(
        ProductEntity(
            key = "0",
            categoryKey = "0",
            name = "키워드1",
            price = 100,
            isFavorite = true
        ),
        ProductEntity(
            key = "1",
            categoryKey = "0",
            name = "키워드2",
            price = 100,
            isFavorite = true
        ),
        ProductEntity(
            key = "2",
            categoryKey = "0",
            name = "키워드3",
            price = 100,
            isFavorite = false
        ),
        ProductEntity(
            key = "3",
            categoryKey = "0",
            name = "키워드4",
            price = 100,
            isFavorite = true
        ),
        ProductEntity(
            key = "4",
            categoryKey = "0",
            name = "none",
            price = 100,
            isFavorite = true
        )
    )

}
