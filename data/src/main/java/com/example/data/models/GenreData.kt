package com.example.data.models


class GenreData(
    val id: String,
    val titles: List<String>,
    val descriptions: List<String>,
    val poster: GenrePosterData
)

data class GenrePosterData(
    var name: String,
    var url: String,
)