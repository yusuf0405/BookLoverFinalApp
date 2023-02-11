package com.example.domain.models


class GenreDomain(
    val id: String,
    val titles: List<String>,
    val descriptions: List<String>,
    val poster: GenrePosterDomain
)

data class GenrePosterDomain(
    var name: String,
    var url: String,
)