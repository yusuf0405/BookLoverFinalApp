package com.example.domain.models.book

data class BooksThatRead(
    var bookId: String,
    var chaptersRead: Int,
    var isReadingPages: List<Boolean>,
    var progress: Int,
    var userId: String,
)
