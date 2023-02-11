package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

const val AUDIO_BOOKS_TABLE_NAME = "audio_books_table"

@Entity(tableName = AUDIO_BOOKS_TABLE_NAME)
class AudioBookCache(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "school_id") val schoolId: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "genres") val genres: List<String>,
    @ColumnInfo(name = "current_start_position") val currentStartPosition: Int,
    @ColumnInfo(name = "audio_book_file") val audioBookFile: AudioBookFileCache,
    @ColumnInfo(name = "audio_book_poster") val audioBookPoster: AudioBookPosterCache
)

data class AudioBookFileCache(
    val name: String,
    val url: String
)

data class AudioBookPosterCache(
    val name: String,
    val url: String
)