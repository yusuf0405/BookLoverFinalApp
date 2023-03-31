package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

private const val GENRE_TABLE_NAME = "genres_table"

@Entity(tableName = GENRE_TABLE_NAME,
    indices = [
        Index("id")
    ])
class GenreCache(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "titles") val titles: List<String>,
    @ColumnInfo(name = "descriptions") val descriptions: List<String>,
    @ColumnInfo(name = "poster") val poster: GenrePosterCache
)

data class GenrePosterCache(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String,
)