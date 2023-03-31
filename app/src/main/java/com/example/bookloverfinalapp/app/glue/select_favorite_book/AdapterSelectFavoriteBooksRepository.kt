package com.example.bookloverfinalapp.app.glue.select_favorite_book

import com.example.bookloverfinalapp.app.utils.extensions.swapElements
import com.example.domain.repository.BooksRepository
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.domain.repositories.SelectFavoriteBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdapterSelectFavoriteBooksRepository @Inject constructor(
    private val booksRepository: BooksRepository
) : SelectFavoriteBooksRepository {

    override fun fetchAllSelectFavoritesBooks(): Flow<List<SelectFavoriteBook>> =
        booksRepository.fetchBooksFromCache().map {
            it.map { book ->
                SelectFavoriteBook(
                    id = book.id,
                    title = book.title,
                    annotations = book.description,
                    posterUrl = book.poster.url,
                    isLiked = false
                )
            }.swapElements()
        }
}

