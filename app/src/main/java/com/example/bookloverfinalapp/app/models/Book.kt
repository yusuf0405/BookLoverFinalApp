package com.example.bookloverfinalapp.app.models

import java.io.Serializable
import java.util.*

data class Book(
    var author: String,
    var createdAt: Date,
    var objectId: String,
    var page: Int,
    var genres: List<String>,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var poster: BookPoster,
    var updatedAt: Date,
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

