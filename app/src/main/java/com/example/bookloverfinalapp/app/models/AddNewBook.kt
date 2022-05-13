package com.example.bookloverfinalapp.app.models

data class AddNewBook(
    var title: String,
    var publicYear: String,
    var page: Int,
    var author: String,
    var poster: BookPoster,
    var book: BookPdf,
    var chapterCount: Int,
)
