package com.example.jyghiretest.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
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
class FavoriteRepositoryTest {

    @get:Rule
    private val dispatcherRule = TestDispatcherRule()

    private lateinit var productDao: ProductDao

    private lateinit var repository: FavoriteRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(
            context, JygDatabase::class.java
        ).build()
        productDao = database.productDao()
        repository = DefaultFavoriteRepository(
            productDao, dispatcherRule.dispatcher.testDispatcherProvider()
        )
    }

    @Test
    fun test_isFavorite_true_after_toggleFavorite() = runTest() {
        val entity = ProductEntity("0", "", "", 100, false)
        productDao.insert(entity)

        repository.searchFavoriteProducts("").test {
            val beforeToggled = awaitItem()
            assert(beforeToggled.isEmpty())

            repository.toggleFavorite("0")

            val afterToggled = awaitItem()
            assert(afterToggled.size == 1)
            assert(afterToggled.all { it.isFavorite })
        }
    }

    @Test
    fun test_searchFavoriteProducts_returns_3_with_keyword_키워드() = runTest {
        val entities = getSearchEntities()
        productDao.insertAll(*entities.toTypedArray())

        repository.searchFavoriteProducts("키워드").test {
            val products = awaitItem()
            assert(products.size == 3)
            assert(products.all { it.isFavorite })
        }
    }

    @Test
    fun test_searchFavoriteProducts_changes_after_toggleFavorite() = runTest {
        val entity1 = ProductEntity("0", "", "", 100, false)
        val entity2 = ProductEntity("1", "", "", 100, false)

        productDao.insertAll(entity1, entity2)

        repository.searchFavoriteProducts("").test {
            val beforeToggled = awaitItem()
            assert(beforeToggled.isEmpty())

            repository.toggleFavorite("0")
            repository.toggleFavorite("1")

            val afterToggled = awaitItem()
            assert(afterToggled.size == 2)
            assert(afterToggled.all { it.isFavorite })
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
