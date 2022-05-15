package com.example.data.models

data class UpdateBookData(
    var author: String,
    var publicYear: String,
    var title: String,
    var poster: BookPosterData,
)
