package com.example.data.cache.source.books_that_read

import com.example.data.cache.models.BookThatReadCache
import com.example.data.models.BookThatReadData
import kotlinx.coroutines.flow.Flow

interface BooksThatReadCacheDataSource {

    fun fetchBooksThatReadObservable(): Flow<List<BookThatReadCache>>

    suspend fun fetchBooksThatReadSingle(): List<BookThatReadCache>

    suspend fun fetchBooksByBookId(bookId: String): BookThatReadCache?

    suspend fun saveBooks(books: List<BookThatReadData>)

    suspend fun deleteBook(id: String)

    suspend fun getMyBook(id: String): BookThatReadCache?

    suspend fun addBook(book: BookThatReadCache)

    suspend fun updateProgress(id: String, progress: Int)

    suspend fun updateChapters(id: String, chapters: Int)

    suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>)

    suspend fun clearTable()
}