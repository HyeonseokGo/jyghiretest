package com.example.jyghiretest.data.utils

class DatabaseSyncer<NETWORK, LOCAL, KEY>(
    private val insert: suspend (LOCAL) -> Unit,
    private val update: suspend (LOCAL) -> Unit,
    private val delete: suspend (LOCAL) -> Unit,
    private val localToKey: Mapper<LOCAL, KEY>,
    private val networkToKey: Mapper<NETWORK, KEY>,
    private val networkToLocal: Mapper<NETWORK, LOCAL>,
) {

    suspend fun run(newItems: List<NETWORK>, oldItems: List<LOCAL>) {

        val itemsToDelete = ArrayList(oldItems)

        newItems.forEach { newItem ->
            val networkItemKey = networkToKey(newItem)

            val existingItem = oldItems.find {
                localToKey(it) == networkItemKey
            }

            if (existingItem != null) {
                update(networkToLocal(newItem))
                itemsToDelete.remove(existingItem)
            } else {
                insert(networkToLocal(newItem))
            }
        }

        itemsToDelete.forEach { itemToDelete ->
            delete(itemToDelete)
        }

    }

}
