package com.example.domain.use_cases

import com.example.domain.sort_dialog.SavedBookSortOrder
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserCacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface FetchAllSortedSavedBooksUseCase {

    operator fun invoke(order: SavedBookSortOrder): Flow<List<BookThatReadDomain>>
}

class FetchAllSortedSavedBooksUseCaseImpl(
    userCacheRepository: UserCacheRepository,
    savedBooksRepository: BookThatReadRepository,
) : FetchAllSortedSavedBooksUseCase {

    override fun invoke(order: SavedBookSortOrder) = userSavedBooksFlow.map { books ->
        fetchSortedBooksByOrder(order = order, books = books)
    }.flowOn(Dispatchers.Default)

    private val currentUserFlow = userCacheRepository
        .fetchCurrentUserFromCacheFlow()
        .flowOn(Dispatchers.IO)

    private val userSavedBooksFlow = currentUserFlow.map { it.id }
        .flowOn(Dispatchers.Default)
        .flatMapLatest(savedBooksRepository::fetchUserAllBooksThatReadByUserId)
        .flowOn(Dispatchers.IO)

    private fun fetchSortedBooksByOrder(
        order: SavedBookSortOrder,
        books: List<BookThatReadDomain>
    ): List<BookThatReadDomain> {
        return when (order) {
            SavedBookSortOrder.BY_DATE -> books.sortedByDescending { it.createdAt }
            SavedBookSortOrder.BY_BOOK_NAME -> books.sortedBy { it.title }
            SavedBookSortOrder.BY_AUTHOR_NAME -> books.sortedBy { it.author }
            SavedBookSortOrder.BY_READING -> books.sortedByDescending { it.progress }
        }
    }
}