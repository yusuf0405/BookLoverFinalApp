package com.example.data.models

data class AddNewBookData(
    var title: String,
    var publicYear: String,
    var page: Int,
    var author: String,
    var poster: BookPosterData,
    var book: BookPdfData,
    var chapterCount: Int,
)