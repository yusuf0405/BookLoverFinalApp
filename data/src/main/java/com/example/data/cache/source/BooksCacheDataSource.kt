package com.example.data.cache.source

import com.example.data.cache.db.BooksDao
import com.example.data.cache.db.BooksThatReadDao
import com.example.data.cache.models.BookCache
import com.example.data.cache.models.BookPosterCache
import com.example.data.models.BookData
import com.example.domain.Mapper
import com.example.domain.models.BookPosterDomain

interface BooksCacheDataSource {

    suspend fun fetchBooks(): List<BookCache>

    suspend fun saveBooks(books: List<BookData>)

    suspend fun addNewBook(book: BookData)

    suspend fun deleteBook(id: String)

    suspend fun deleteMyBookIsCache(id: String)

    suspend fun clearTable()

    suspend fun updateTitle(id: String, title: String)

    suspend fun updateAuthor(id: String, author: String)

    suspend fun updatePublicYear(id: String, publicYear: String)

    suspend fun updatePoster(id: String, poster: BookPosterDomain)

    class Base(
        private val bookDao: BooksDao,
        private val bookThatReadDao: BooksThatReadDao,
        private val dataMapper: Mapper<BookData, BookCache>,
    ) : BooksCacheDataSource {

        override suspend fun fetchBooks() = bookDao.getAllBooks()

        override suspend fun saveBooks(books: List<BookData>) {
            books.forEach { book -> bookDao.addNewBook(book = dataMapper.map(book)) }
        }

        override suspend fun addNewBook(book: BookData) {
            bookDao.addNewBook(dataMapper.map(book))
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
            bookDao.updateBookPoster(poster = BookPosterCache(name = poster.name, url = poster.url),
                id = id)

    }
}