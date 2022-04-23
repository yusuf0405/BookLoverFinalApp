package com.example.domain.models.book

data class AddNewBookRequest(
    var progress: Int,
    var bookId: String,
    var userId: String,
    var chaptersRead: Int,
    var isReadingPages: List<Boolean>,
)