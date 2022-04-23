package com.example.domain.domain.models

import java.util.*

data class StudentBookDomain(
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: StudentBookPosterDomain,
    var updatedAt: String,
    var book: StudentBookPdfDomain,
    var progress: Int,
    var isReadingPages: List<Boolean>,
)

data class StudentBookPosterDomain(
    var name: String,
    var url: String,
)

data class StudentBookPdfDomain(
    var name: String,
    var url: String,
)
