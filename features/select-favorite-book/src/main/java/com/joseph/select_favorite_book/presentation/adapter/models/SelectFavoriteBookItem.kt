package com.joseph.select_favorite_book.presentation.adapter.models

import com.joseph.ui_core.adapter.Item

data class SelectFavoriteBookItem(
    val id: String,
    val title: String,
    val annotations: String,
    val posterUrl: String,
    val isLiked: Boolean
) : Item
