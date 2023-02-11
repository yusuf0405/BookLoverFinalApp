package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BooksRepository {

    fun fetchAllBooks(schoolId: String, userId: String): Flow<List<BookDomain>>

    fun fetchBookObservable(bookId: String): Flow<BookDomain>

    fun fetchBooksFromCache(): Flow<List<BookDomain>>

    suspend fun fetchBookFromIdInCache(bookId: String): BookDomain

    fun fetchBooksFromCloud(schoolId: String, userId: String): Flow<List<BookDomain>>

    suspend fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): RequestState<List<BookQuestionDomain>>

    suspend fun addNewBook(book: AddNewBookDomain): RequestState<Unit>

    suspend fun addBookQuestion(question: AddBookQuestionDomain): RequestState<Unit>

    suspend fun fetchBookPdfFileForReading(url: String): RequestState<InputStream>

    suspend fun deleteBook(id: String): RequestState<Unit>

    suspend fun deleteBookQuestion(id: String): RequestState<Unit>

    suspend fun updateBookQuestion(id: String, question: AddBookQuestionDomain): RequestState<Unit>

    suspend fun updateBook(id: String, book: UpdateBookDomain): RequestState<Unit>

    suspend fun clearBooksCache()

}