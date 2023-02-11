package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

private const val USER_STATISTICS_TABLE_NAME = "user_statistics"

@Entity(tableName = USER_STATISTICS_TABLE_NAME)
data class UserStatisticCache(
    @PrimaryKey val day: Int,
    @ColumnInfo(name = "progress") var progress: Int,
)