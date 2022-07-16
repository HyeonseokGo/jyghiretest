package com.example.jyghiretest.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.jyghiretest.data.AppDataRepository
import com.example.jyghiretest.data.DefaultAppDataRepository
import com.example.jyghiretest.data.TestDispatcherRule
import com.example.jyghiretest.data.database.CategoryDao
import com.example.jyghiretest.data.database.JygDatabase
import com.example.jyghiretest.data.database.ProductDao
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity
import com.example.jyghiretest.data.network.AppDataFetcher
import com.example.jyghiretest.data.network.RetrofitAppDataFetcher
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.network.model.ProductListResponse
import com.example.jyghiretest.data.network.model.ProductResponse
import com.example.jyghiretest.data.testdoubles.FakeAppDataFetcher
import com.example.jyghiretest.data.testdoubles.FakeJygApi
import com.example.jyghiretest.data.testdoubles.testDispatcherProvider
import com.example.jyghiretest.data.utils.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppDataRepositoryTest {

    @get:Rule
    private val testDispatcherRule = TestDispatcherRule()

    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao

    private lateinit var repository: AppDataRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database =
            Room.inMemoryDatabaseBuilder(context, JygDatabase::class.java).build()
        productDao = database.productDao()
        categoryDao = database.categoryDao()
        repository = createRepository()
    }

    @Test
    fun test_project_and_category_inserted() = runTest {
        assert(categoryDao.getAllImmediately().isEmpty())
        assert(productDao.getAllImmediately().isEmpty())

        repository.sync()

        categoryDao.getAllImmediately().also {
            assert(it.isNotEmpty())
        }
        productDao.getAllImmediately().also {
            assert(it.isNotEmpty())
        }
    }

    @Test
    fun test_delete_all_after_sync() = runTest {
        val productEntity1 = ProductEntity(
            key = "key0", categoryKey = "C0", name = "product1", price = 1000, liked = false
        )
        val categoryEntity1 = CategoryEntity(key = "C0", name = "category1")
        categoryDao.insert(categoryEntity1)
        productDao.insert(productEntity1)

        assert(categoryDao.getAllImmediately().size == 1)
        assert(productDao.getAllImmediately().size == 1)

        val repository =
            createRepository(FakeAppDataFetcher(ProductListResponse(listOf(), listOf())))
        repository.sync()

        assert(categoryDao.getAllImmediately().isEmpty())
        assert(productDao.getAllImmediately().isEmpty())
    }

    @Test
    fun test_product_insert_1_update_2_delete_3() = runTest {
        val categoryKey = listOf("c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9")
        val categoryEntities = categoryKey.mapIndexed { index, s ->
            CategoryEntity(key = s, name = "$index")
        }

        val productKeys = listOf("p0", "p1", "p2", "p3", "p4", "p5")
        val productEntities = productKeys.take(5).mapIndexed { index, s ->
            ProductEntity(
                key = s,
                categoryKey = categoryKey[index],
                name = "name$s",
                price = index * 1000,
                liked = false
            )
        }

        categoryDao.insertAll(*categoryEntities.toTypedArray())
        productDao.insertAll(*productEntities.toTypedArray())

        assert(productDao.getAllImmediately().size == 5)

        val productResponses = productKeys.takeLast(3).mapIndexed { index, s ->
            ProductResponse(
                key = s,
                categoryKey = categoryKey[index],
                name = "name${s}changed",
                price = index * 1000,
            )
        }
        val categoryResponses = categoryKey.mapIndexed { index, s ->
            CategoryResponse(key = s, name = "$index")
        }

        val fetcher = FakeAppDataFetcher(ProductListResponse(categoryResponses, productResponses))
        val repository = createRepository(fetcher)

        repository.sync()

        productDao.getAllImmediately().also { list ->
            assert(list.size == 3)
            assert(list.none { it.key == "p0" })
            assert(list.none { it.key == "p1" })
            assert(list.none { it.key == "p2" })
            assert(list.find { it.key == "p3" }?.name == "p3changed")
            assert(list.find { it.key == "p4" }?.name == "p4changed")
        }

    }

    @Test
    fun test_product_with_same_key_name_and_price_changes() = runTest {
        val keys = listOf("0", "1", "2")
        val productEntities = keys.map {
            ProductEntity(
                key = it,
                categoryKey = "categoryKey$it",
                name = "name$it",
                price = 100,
                liked = true
            )
        }
        val productResponse = keys.map {
            ProductResponse(
                key = it,
                categoryKey = "categoryKey$it",
                name = "changed",
                price = 1000
            )
        }

        productDao.insertAll(*productEntities.toTypedArray())

        val fetcher = FakeAppDataFetcher(ProductListResponse(listOf(), productResponse))
        val repository = createRepository(fetcher)
        repository.sync()

        productDao.getAllImmediately().also { list ->
            list.all { it.price == 1000 }
            list.all { it.name == "changed" }
            list.all { it.liked }
        }
    }

    @Test
    fun test_product_like_not_changes() = runTest {
        val productKeysLikeChanges = listOf("0", "2", "4")
        val productKeysLikeNotChanges = listOf("1", "3")
        val keys = productKeysLikeChanges + productKeysLikeNotChanges
        val productEntities = keys.sorted().map {
            ProductEntity(
                key = it,
                name = "name$it",
                categoryKey = "categoryKey$it",
                price = 100,
                liked = productKeysLikeChanges.contains(it)
            )
        }

        productDao.insertAll(*productEntities.toTypedArray())

        assert(productDao.getAllImmediately().size == productEntities.size)

        val productResponse = keys.sorted().map {
            ProductResponse(key = it, name = it, categoryKey = it, price = 100)
        }
        val fetcher = FakeAppDataFetcher(ProductListResponse(listOf(), productResponse))
        val repository = createRepository(fetcher)

        repository.sync()

        productDao.getAllImmediately().also { list ->
            list.filter { productKeysLikeChanges.contains(it.key) }.all { it.liked }
            list.filter { productKeysLikeNotChanges.contains(it.key) }.none { it.liked }
        }
    }


    private fun createRepository(
        fetcher: AppDataFetcher = RetrofitAppDataFetcher(
            FakeJygApi,
            testDispatcherRule.dispatcher.testDispatcherProvider()
        )
    ): DefaultAppDataRepository {
        val productSyncer = DatabaseSyncer(
            insert = productDao::insert,
            update = productDao::update,
            delete = productDao::delete,
            localToKey = ProductEntityToKey(),
            networkToKey = ProductResponseToKey(),
            networkToLocal = ProductResponseToEntity(productDao)
        )

        val categorySyncer = DatabaseSyncer(
            insert = categoryDao::insert,
            update = categoryDao::update,
            delete = categoryDao::delete,
            localToKey = CategoryEntityToKey(),
            networkToKey = CategoryResponseToKey(),
            networkToLocal = CategoryResponseToEntity()
        )

        return DefaultAppDataRepository(
            categorySyncer,
            productSyncer,
            categoryDao,
            productDao,
            fetcher
        )
    }

}
