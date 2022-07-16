package com.example.jyghiretest.data.database

import androidx.room.*
import com.example.jyghiretest.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM categoryentity WHERE :key = `key`")
    suspend fun get(key: String): CategoryEntity?

    @Query("SELECT * FROM categoryentity")
    suspend fun getAllImmediately(): List<CategoryEntity>

    @Query("SELECT * FROM categoryentity")
    fun getAll(): Flow<List<CategoryEntity>>

}
