package com.example.domain.models

data class AddNewBookDomain(
    var title: String,
    var publicYear: String,
    var schoolId: String,
    var genres: List<String>,
    var page: Int,
    var author: String,
    var poster: BookPosterDomain,
    var book: BookPdfDomain,
    var chapterCount: Int,
)