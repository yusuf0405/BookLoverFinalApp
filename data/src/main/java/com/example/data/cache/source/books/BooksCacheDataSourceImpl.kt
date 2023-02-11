package com.example.data.cache.source.books

import com.example.data.cache.db.BooksDao
import com.example.data.cache.db.BooksThatReadDao
import com.example.data.cache.models.BookCache
import com.example.data.cache.models.BookPosterCache
import com.example.data.cache.models.SavedStatusCache
import com.example.data.models.BookData
import com.example.domain.Mapper
import com.example.domain.models.BookPosterDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BooksCacheDataSourceImpl @Inject constructor(
    private val bookDao: BooksDao,
    private val bookThatReadDao: BooksThatReadDao,
    private val bookDataToCacheMapper: Mapper<BookData, BookCache>,
) : BooksCacheDataSource {

    override fun fetchAllBooksObservable() = bookDao.fetchAllBooksObservable()

    override fun fetchBookObservable(bookId: String): Flow<BookCache?> =
        bookDao.fetchBookObservable(bookId = bookId)
            .flowOn(Dispatchers.IO)

    override suspend fun fetchAllBooksSingle(): List<BookCache> = bookDao.fetchAllBooksSingle()

    override suspend fun fetchBookFromId(bookId: String): BookCache =
        bookDao.fetchBookFromId(bookId = bookId)

    override suspend fun updateBookSavedStatus(
        savedStatusCache: SavedStatusCache,
        bookId: String
    ) {
        bookDao.updateCacheBookSavedStatus(savedStatus = savedStatusCache, id = bookId)
    }

    override suspend fun saveBooks(books: List<BookData>) {
        books.map(bookDataToCacheMapper::map).forEach { book ->
            bookDao.addNewBook(book = book)
        }
    }

    override suspend fun addNewBook(book: BookData) {
        bookDao.addNewBook(bookDataToCacheMapper.map(book))
    }

    override suspend fun deleteBook(id: String) = bookDao.deleteById(id = id)

    override suspend fun deleteMyBookIsCache(id: String) {
        bookThatReadDao.deleteById(id = id)
    }

    override suspend fun clearTable() = bookDao.clearTable()

    override suspend fun updateTitle(id: String, title: String) =
        bookDao.updateBookTitle(id = id, title = title)

    override suspend fun updateAuthor(id: String, author: String) =
        bookDao.updateBookAuthor(id = id, author = author)

    override suspend fun updatePublicYear(id: String, publicYear: String) =
        bookDao.updateBookPublicYear(publicYear = publicYear, id = id)

    override suspend fun updatePoster(id: String, poster: BookPosterDomain) =
        bookDao.updateBookPoster(
            poster = BookPosterCache(name = poster.name, url = poster.url),
            id = id
        )
}