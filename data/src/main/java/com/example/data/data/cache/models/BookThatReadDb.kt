package com.example.data.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "book_that_read_database")
data class BookThatReadDb(
    @PrimaryKey var objectId: String,
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: BookThatReadPosterDb,
    var updatedAt: Date,
    var book: String,
    var progress: Int,
    var isReadingPages: List<Boolean>,
)

data class BookThatReadPosterDb(
    var name: String,
    var url: String,
)
