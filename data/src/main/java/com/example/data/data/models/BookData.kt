package com.example.data.data.models

class BookData(
    var author: String,
    var createdAt: String,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfData,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterData,
    var updatedAt: String,
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
