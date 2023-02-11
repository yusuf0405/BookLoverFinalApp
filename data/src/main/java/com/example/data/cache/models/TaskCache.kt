package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val TASKS_TABLE_NAME = "tasks_table"

@Entity(tableName = TASKS_TABLE_NAME)
data class TaskCache(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "class_id") val classId: String,
    @ColumnInfo(name = "start_date") val startDate: Date,
    @ColumnInfo(name = "end_date") val endDate: Date,
    @ColumnInfo(name = "task_genres") val taskGenres: List<String>,
)