package com.example.bookloverfinalapp.app.models

import java.io.Serializable

data class Book(
    var author: String,
    var createdAt: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var poster: BookPoster,
    var updatedAt: String,
    var book: BookPdf,
) : Serializable

data class BookPdf(
    var name: String,
    var url: String,
)

data class BookPoster(
    var name: String,
    var url: String,
)

