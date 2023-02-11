package com.example.domain.use_cases

import com.example.domain.sort_dialog.BookSortOrder
import com.example.domain.models.BookDomain
import com.example.domain.models.SavedStatusDomain
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface FetchAllSortedBooksUseCase {

    operator fun invoke(
        schoolId: String,
        userId: String,
        order: BookSortOrder
    ): Flow<List<BookDomain>>

}

class FetchAllSortedBooksUseCaseImpl(
    private val booksRepository: BooksRepository,
) : FetchAllSortedBooksUseCase {

    override fun invoke(
        schoolId: String,
        userId: String,
        order: BookSortOrder
    ): Flow<List<BookDomain>> =
        booksRepository.fetchAllBooks(schoolId = schoolId, userId = userId).map { books ->
            fetchSortedBooksByOrder(order = order, books = books)
        }.flowOn(Dispatchers.Default)

    private fun fetchSortedBooksByOrder(
        order: BookSortOrder,
        books: List<BookDomain>
    ) = when (order) {
        BookSortOrder.BY_DATE -> books.sortedByDescending { it.createdAt }
        BookSortOrder.BY_CACHED -> sortByCached(books)
        BookSortOrder.BY_BOOK_NAME -> books.sortedBy { it.title }
        BookSortOrder.BY_AUTHOR_NAME -> books.sortedBy { it.author }
    }

    private fun sortByCached(books: List<BookDomain>): MutableList<BookDomain> {
        val savedBooks = books.filter { it.savedStatus == SavedStatusDomain.SAVED }
        val savingBooks = books.filter { it.savedStatus == SavedStatusDomain.SAVING }
        val notSavedBooks = books.filter { it.savedStatus == SavedStatusDomain.NOT_SAVED }
        val filteredBook = mutableListOf<BookDomain>()
        filteredBook.addAll(savedBooks)
        filteredBook.addAll(savingBooks)
        filteredBook.addAll(notSavedBooks)
        return filteredBook
    }
}