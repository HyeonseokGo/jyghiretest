package com.example.jyghiretest.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchQueryStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_query")

    private val LAST_SEARCH_QUERY = stringPreferencesKey("example_counter")

    suspend fun save(keyword: String) {
        context.dataStore.edit { searchQuery ->
            searchQuery[LAST_SEARCH_QUERY] = keyword
        }
    }

    fun lastSearchQuery(): Flow<String> = context.dataStore.data.map { searchQuery ->
        searchQuery[LAST_SEARCH_QUERY] ?: ""
    }

}
