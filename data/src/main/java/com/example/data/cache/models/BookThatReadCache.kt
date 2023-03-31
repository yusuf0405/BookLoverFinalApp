package com.example.data.cache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "books_that_read",
    indices = [
        Index("objectId"),
        Index("progress","objectId"),
        Index("chapters_read","objectId"),
        Index("is_reading_pages","objectId"),
    ]
)
data class BookThatReadCache(
    @PrimaryKey var objectId: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "created_at") var createdAt: Date,
    @ColumnInfo(name = "book_id") var bookId: String,
    @ColumnInfo(name = "page") var page: Int,
    @ColumnInfo(name = "public_year") var publicYear: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "chapter_count") var chapterCount: Int,
    @ColumnInfo(name = "chapters_read") var chaptersRead: Int,
    @ColumnInfo(name = "poster") var poster: BookThatReadPosterCache,
    @ColumnInfo(name = "updated_at") var updatedAt: Date,
    @ColumnInfo(name = "book") var book: String,
    @ColumnInfo(name = "progress") var progress: Int,
    @ColumnInfo(name = "is_reading_pages") var isReadingPages: List<Boolean>,
) {

    companion object {
        fun unknown() = BookThatReadCache(
            author = String(),
            createdAt = Date(),
            updatedAt = Date(),
            bookId = String(),
            objectId = String(),
            publicYear = String(),
            page = 0,
            title = String(),
            progress = 0,
            chapterCount = 0,
            chaptersRead = 0,
            poster = BookThatReadPosterCache(String(), String()),
            book = String(),
            isReadingPages = emptyList()
        )
    }
}

data class BookThatReadPosterCache(
    var name: String,
    var url: String,
)
