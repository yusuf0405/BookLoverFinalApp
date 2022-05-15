package com.example.domain.models

data class UpdateBookDomain(
    var author: String,
    var publicYear: String,
    var title: String,
    var poster: BookPosterDomain,
)