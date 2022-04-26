package com.example.bookloverfinalapp.app.models

data class AddNewBookModel(
    val bookId: String,
    val userId: String,
    val page: Int,
    val publicYear: String,
    val book: StudentBookPdf,
    val title: String,
    val chapterCount: Int,
    val poster: StudentBookPoster,
    val isReadingPages: List<Boolean>,
    val chaptersRead: Int,
    val progress: Int,
    val author: String,
)
