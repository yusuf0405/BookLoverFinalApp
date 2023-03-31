package com.joseph.select_favorite_book.domain.models

data class SelectFavoriteBook(
    val id: String,
    val title: String,
    val annotations: String,
    val posterUrl: String,
    val isLiked: Boolean
)
