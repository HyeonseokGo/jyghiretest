package com.example.jyghiretest.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey val key: String,
    @ColumnInfo(name = "name") val name: String
)
