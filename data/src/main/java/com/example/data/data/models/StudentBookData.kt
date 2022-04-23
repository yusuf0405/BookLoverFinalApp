package com.example.data.data.models

import java.util.*

data class StudentBookData(
    var author: String,
    var createdAt: Date,
    var bookId: String,
    var objectId: String,
    var page: Int,
    var publicYear: String,
    var title: String,
    var chapterCount: Int,
    var chaptersRead: Int,
    var poster: StudentBookPosterData,
    var updatedAt: String,
    var book: StudentBookPdfData,
    var progress: Int,
    var isReadingPages: List<Boolean>,
)

data class StudentBookPdfData(
    var name: String,
    var url: String,
)

data class StudentBookPosterData(
    var name: String,
    var url: String,
)