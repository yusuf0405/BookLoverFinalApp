package com.example.data.data.cache.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_database")
data class BookDb(
    @PrimaryKey var id: String,
    var author: String,
    var createdAt: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfDb,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterDb,
    var updatedAt: String,
)

data class BookPdfDb(
    var name: String,
    var type: String,
    var url: String,
)

data class BookPosterDb(
    var name: String,
    var url: String,
)
