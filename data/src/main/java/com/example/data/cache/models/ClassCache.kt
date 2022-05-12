package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class_database")
data class ClassCache(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "title") var title: String,
)
