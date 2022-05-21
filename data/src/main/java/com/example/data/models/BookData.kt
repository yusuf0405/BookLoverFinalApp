package com.example.data.models

import java.util.*

class BookData(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfData,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterData,
    var updatedAt: Date,
    var genres: List<String>,
)

data class BookPdfData(
    var name: String,
    var type: String,
    var url: String,
)

data class BookPosterData(
    var name: String,
    var url: String,
)
