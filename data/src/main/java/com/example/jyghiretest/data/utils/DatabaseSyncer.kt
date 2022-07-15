package com.example.jyghiretest.data.utils

class DatabaseSyncer<NETWORK, LOCAL, KEY>(
    private val insert: suspend (LOCAL) -> Unit,
    private val update: suspend (LOCAL) -> Unit,
    private val delete: suspend (LOCAL) -> Unit,
    private val localToKey: Mapper<LOCAL, KEY>,
    private val networkToKey: Mapper<NETWORK, KEY>,
    private val networkToLocal: Mapper<NETWORK, LOCAL>,
) {

    suspend fun run(
        newItems: List<NETWORK>,
        oldItems: List<LOCAL>
    ): SyncerResult<LOCAL> {

        val insertedItems = mutableListOf<LOCAL>()
        val updatedItems = mutableListOf<LOCAL>()
        val itemsToDelete = ArrayList(oldItems)

        newItems.forEach { newItem ->
            val networkItemKey = networkToKey(newItem)

            val existingItem = oldItems.find {
                localToKey(it) == networkItemKey
            }
            val entity = networkToLocal(newItem)
            if (existingItem != null) {
                update(entity)
                updatedItems.add(entity)
                itemsToDelete.remove(existingItem)
            } else {
                insert(entity)
                insertedItems.add(entity)
            }
        }

        itemsToDelete.forEach { itemToDelete ->
            delete(itemToDelete)
        }

        return SyncerResult(insertedItems, updatedItems, itemsToDelete)
    }

}

data class SyncerResult<T>(
    val inserted: List<T>,
    val updated: List<T>,
    val deleted: List<T>
)
