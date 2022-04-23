package com.example.domain.domain.models

data class BookDomain(
    var author: String,
    var createdAt: String,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfDomain,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterDomain,
    var updatedAt: String,
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