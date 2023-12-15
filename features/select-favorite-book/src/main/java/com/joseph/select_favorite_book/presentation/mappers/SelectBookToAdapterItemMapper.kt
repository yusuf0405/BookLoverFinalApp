package com.joseph.select_favorite_book.presentation.mappers

import com.joseph.common.Mapper
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.presentation.adapter.models.SelectFavoriteBookItem
import javax.inject.Inject

class SelectBookToAdapterItemMapper @Inject constructor() :
    Mapper<SelectFavoriteBook, SelectFavoriteBookItem> {

    override fun map(from: SelectFavoriteBook): SelectFavoriteBookItem = from.run {
        SelectFavoriteBookItem(
            id = id,
            title = title,
            annotations = annotations,
            posterUrl = posterUrl,
            isLiked = isLiked
        )
    }
}