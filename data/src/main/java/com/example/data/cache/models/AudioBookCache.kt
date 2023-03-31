package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

const val AUDIO_BOOKS_TABLE_NAME = "audio_books_table"

@Entity(
    tableName = AUDIO_BOOKS_TABLE_NAME,
    indices = [
        Index("id"),
        Index("is_playing", "id"),
    ]
)
class AudioBookCache(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "is_exclusive") val isExclusive: Boolean,
    @ColumnInfo(name = "school_id") val schoolId: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "genres") val genres: List<String>,
    @ColumnInfo(name = "current_start_position") val currentStartPosition: Int,
    @ColumnInfo(name = "audio_book_file") val audioBookFile: AudioBookFileCache,
    @ColumnInfo(name = "audio_book_poster") val audioBookPoster: AudioBookPosterCache,
    @ColumnInfo(name = "is_playing") val isPlaying: Boolean,
) {
    companion object {
        val unknown = AudioBookCache(
            id = String(),
            title = String(),
            author = String(),
            schoolId = String(),
            description = String(),
            createdAt = Date(),
            currentStartPosition = 0,
            genres = emptyList(),
            audioBookFile = AudioBookFileCache.unknown,
            audioBookPoster = AudioBookPosterCache.unknown,
            isExclusive = false,
            isPlaying = false
        )
    }
}

data class AudioBookFileCache(
    val name: String,
    val url: String
) {
    companion object {
        val unknown = AudioBookFileCache(
            name = String(),
            url = String(),
        )
    }
}

data class AudioBookPosterCache(
    val name: String,
    val url: String
) {
    companion object {
        val unknown = AudioBookPosterCache(
            name = String(),
            url = String(),
        )
    }
}