package com.example.jyghiretest.data.database

import androidx.room.*
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity)

    @Update
    suspend fun update(productEntity: ProductEntity)

    @Delete
    suspend fun delete(productEntity: ProductEntity)

    @Query("SELECT * FROM productentity  WHERE :key = `key`")
    suspend fun get(key: String): ProductEntity?

    @Query("SELECT * FROM productentity")
    suspend fun getAllImmediately(): List<ProductEntity>

    @Query("SELECT * FROM productentity")
    fun getAll(): Flow<List<ProductEntity>>

}
