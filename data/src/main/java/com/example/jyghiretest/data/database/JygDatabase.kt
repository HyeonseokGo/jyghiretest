package com.example.jyghiretest.data.database

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import com.example.jyghiretest.data.database.entity.CategoryEntity
import com.example.jyghiretest.data.database.entity.ProductEntity

@Database(
    entities = [CategoryEntity::class, ProductEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = JygDatabase.LikedToIsFavoriteMigration1::class
        )
    ],
    exportSchema = true
)
abstract class JygDatabase : RoomDatabase() {

    @RenameColumn(fromColumnName = "liked", toColumnName = "isFavorite", tableName = "ProductEntity")
    class LikedToIsFavoriteMigration1 : AutoMigrationSpec

    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "JYG_DATABASE"
    }

}
