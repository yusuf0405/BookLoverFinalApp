package com.example.data.cache.source.books

import com.example.data.cache.models.BookCache
import com.example.data.cache.models.SavedStatusCache
import com.example.data.models.BookData
import com.example.domain.models.BookPosterDomain
import kotlinx.coroutines.flow.Flow

interface BooksCacheDataSource {

    fun fetchAllBooksObservable(): Flow<List<BookCache>>

    fun fetchBookObservable(bookId: String): Flow<BookCache?>

    suspend fun fetchAllBooksSingle(): List<BookCache>

    suspend fun fetchBookFromId(bookId: String): BookCache

    suspend fun saveBooks(books: List<BookData>)

    suspend fun addNewBook(book: BookData)

    suspend fun deleteBook(id: String)

    suspend fun deleteMyBookIsCache(id: String)

    suspend fun clearTable()

    suspend fun updateTitle(id: String, title: String)

    suspend fun updateBookSavedStatus(savedStatusCache: SavedStatusCache, bookId: String)

    suspend fun updateAuthor(id: String, author: String)

    suspend fun updatePublicYear(id: String, publicYear: String)

    suspend fun updatePoster(id: String, poster: BookPosterDomain)

}