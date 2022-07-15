package com.example.jyghiretest.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity

@Database(entities = [CategoryEntity::class, ProductEntity::class], version = 1)
abstract class JygDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "JYG_DATABASE"
    }

}
