package com.example.bookloverfinalapp.app.models

data class AddNewBookModel(
    val bookId: String,
    val userId: String,
    val page: Int,
    val publicYear: String,
    val book: String,
    val title: String,
    val chapterCount: Int,
    val poster: BookThatReadPoster,
    val isReadingPages: List<Boolean>,
    val chaptersRead: Int,
    val progress: Int,
    val author: String,
)
