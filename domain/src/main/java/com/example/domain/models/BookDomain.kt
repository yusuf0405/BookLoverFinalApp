package com.example.domain.models

import java.util.*

data class BookDomain(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfDomain,
    var title: String,
    var genres: List<String>,
    var chapterCount: Int,
    var poster: BookPosterDomain,
    var updatedAt: Date,
)

class BookPdfDomain(
    var name: String,
    var type: String,
    var url: String,
)

class BookPosterDomain(
    var name: String,
    var url: String,
)