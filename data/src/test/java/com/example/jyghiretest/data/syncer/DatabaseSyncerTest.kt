package com.example.jyghiretest.data.syncer

import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.di.CategoryDatabaseSyncer
import com.example.jyghiretest.data.fakeCategoryResponses
import com.example.jyghiretest.data.network.model.CategoryResponse
import com.example.jyghiretest.data.toEntities
import com.example.jyghiretest.data.utils.CategoryEntityToKey
import com.example.jyghiretest.data.utils.CategoryResponseToEntity
import com.example.jyghiretest.data.utils.CategoryResponseToKey
import com.example.jyghiretest.data.utils.DatabaseSyncer
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DatabaseSyncerTest {

    private lateinit var syncer: CategoryDatabaseSyncer

    @Before
    fun setUp() {
        syncer = createSyncer()
    }

    @Test
    fun test_insert_all() = runTest {
        syncer.runTest(fakeCategoryResponses, listOf()) { inserted, _, _ ->
            assert(inserted.size == fakeCategoryResponses.size)
        }
    }

    @Test
    fun test_delete_all() = runTest {
        syncer.runTest(listOf(), fakeCategoryResponses.toEntities()) { _, updated, deleted ->
            assert(deleted.size == fakeCategoryResponses.size)
            assert(updated.isEmpty())
        }
    }

    @Test
    fun test_delete_and_left_3() = runTest {
        val networks = fakeCategoryResponses.take(3)
        val expectedDeletedCount = fakeCategoryResponses.size - 3

        syncer.runTest(networks, fakeCategoryResponses.toEntities()) { inserted, updated, deleted ->
            assert(deleted.size == expectedDeletedCount)
            assert(updated.size == networks.size)
            assert(inserted.isEmpty())
        }
    }

    @Test
    fun test_update_all() = runTest {
        syncer.runTest(
            fakeCategoryResponses,
            fakeCategoryResponses.toEntities()
        ) { inserted, updated, deleted ->
            assert(inserted.isEmpty())
            assert(updated.size == fakeCategoryResponses.size)
            assert(deleted.isEmpty())
        }
    }

    @Test
    fun test_insert_1_update_2_delete_3() = runTest {
        val insert1 = CategoryResponse("insert1", "")
        val update1Network = CategoryResponse("update1", "updated")
        val update2Network = CategoryResponse("update2", "updated")
        val update1Local = CategoryEntity("update1", "not updated")
        val update2Local = CategoryEntity("update2", "not updated")
        val deletes = listOf(
            CategoryEntity("delete1", ""),
            CategoryEntity("delete2", ""),
            CategoryEntity("delete3", "")
        )
        val networks = listOf(insert1, update1Network, update2Network)
        val locals = listOf(update1Local, update2Local) + deletes
        syncer.runTest(networks, locals) {inserted, updated, deleted ->
            assert(inserted.size == 1)
            assert(updated.size == 2)
            assert(deleted.size == 3)
            assert(updated.find { it.key == "update1" }?.name == "updated")
            assert(updated.find { it.key == "update2" }?.name == "updated")
        }
    }

    private suspend fun CategoryDatabaseSyncer.runTest(
        networks: List<CategoryResponse>,
        locals: List<CategoryEntity>,
        assertions: (inserted: List<CategoryEntity>, updated: List<CategoryEntity>, deleted: List<CategoryEntity>) -> Unit
    ) {
        this.run(
            networks, locals
        ).also { (inserted, updated, deleted) ->
            assertions(inserted, updated, deleted)
        }
    }

    private fun createSyncer() = DatabaseSyncer(
        insert = {},
        update = {},
        delete = {},
        localToKey = CategoryEntityToKey(),
        networkToKey = CategoryResponseToKey(),
        networkToLocal = CategoryResponseToEntity()
    )

}
