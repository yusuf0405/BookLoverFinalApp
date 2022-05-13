package com.example.data.cache.source

import com.example.data.cache.db.BooksDao
import com.example.data.cache.models.BookDb
import com.example.data.models.BookData
import com.example.domain.Mapper

interface BooksCacheDataSource {

    suspend fun fetchBooks(): List<BookDb>

    suspend fun saveBooks(books: List<BookData>)

    suspend fun addNewBook(book: BookData)

    suspend fun clearTable()

    class Base(
        private val bookDao: BooksDao,
        private val dataMapper: Mapper<BookData, BookDb>,
    ) : BooksCacheDataSource {
        override suspend fun fetchBooks() = bookDao.getAllBooks()

        override suspend fun saveBooks(books: List<BookData>) {
            books.forEach { book -> bookDao.addNewBook(book = dataMapper.map(book)) }
        }

        override suspend fun addNewBook(book: BookData) {
            bookDao.addNewBook(dataMapper.map(book))
        }

        override suspend fun clearTable() = bookDao.clearTable()

    }
}