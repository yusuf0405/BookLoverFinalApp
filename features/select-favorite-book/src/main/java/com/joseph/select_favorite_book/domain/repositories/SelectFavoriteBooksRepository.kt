package com.joseph.select_favorite_book.domain.repositories

import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import kotlinx.coroutines.flow.Flow

interface SelectFavoriteBooksRepository {

    fun fetchAllSelectFavoritesBooks(): Flow<List<SelectFavoriteBook>>
}