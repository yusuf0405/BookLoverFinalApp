package com.example.data.models

import java.util.*

data class BookThatReadData(
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: BookThatReadPosterData,
    var updatedAt: Date,
    var book: String,
    var progress: Int,
    var isReadingPages: List<Boolean>,
)
data class BookThatReadPosterData(
    var name: String,
    var url: String,
)