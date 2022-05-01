package com.example.domain.domain.models

data class AddNewBookDomain(
    val bookId: String,
    val userId: String,
    val page: Int,
    val publicYear: String,
    val book: String,
    val title: String,
    val chapterCount: Int,
    val poster: BookThatReadPosterDomain,
    val isReadingPages: List<Boolean>,
    val chaptersRead: Int,
    val progress: Int,
    val author: String,
)