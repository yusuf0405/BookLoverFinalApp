package com.joseph.select_favorite_book.domain.usecase

import com.joseph.common.EmptyListException
import com.joseph.select_favorite_book.domain.models.SelectFavoriteBook
import com.joseph.select_favorite_book.domain.repositories.SelectFavoriteBooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

interface FetchSelectFavoriteBooksUseCase {

    operator fun invoke(): Flow<List<SelectFavoriteBook>>
}

class FetchSelectFavoriteBooksUseCaseImpl @Inject constructor(
    private val repository: SelectFavoriteBooksRepository
) : FetchSelectFavoriteBooksUseCase {

    override fun invoke(): Flow<List<SelectFavoriteBook>> =
        repository.fetchAllSelectFavoritesBooks()
            .onEach(::handleReceivedSelectFavoriteBooks)

    private fun handleReceivedSelectFavoriteBooks(selectFavoriteBooks: List<SelectFavoriteBook>) {
        if (selectFavoriteBooks.isEmpty()) throw EmptyListException()
    }
}