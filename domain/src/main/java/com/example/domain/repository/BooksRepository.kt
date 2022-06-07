package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.*
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BooksRepository {

    fun fetchBooks(schoolId: String): Flow<Resource<List<BookDomain>>>

    fun onRefresh(schoolId: String): Flow<Resource<List<BookDomain>>>

    fun fetchSimilarBooks(genres: List<String>, bookId: String): Flow<Resource<List<BookDomain>>>

    fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestionDomain>>>

    fun fetchSearchBook(searchText: String, schoolId: String): Flow<Resource<List<BookDomain>>>


    fun addNewBook(book: AddNewBookDomain): Flow<Resource<Unit>>

    fun addBookQuestion(question: AddBookQuestionDomain): Flow<Resource<Unit>>

    fun getBookForReading(url: String): Flow<Resource<InputStream>>

    fun deleteBook(id: String): Flow<Resource<Unit>>

    fun deleteBookQuestion(id: String): Flow<Resource<Unit>>

    fun updateBookQuestion(id: String, question: AddBookQuestionDomain): Flow<Resource<Unit>>

    fun updateBook(id: String, book: UpdateBookDomain): Flow<Resource<Unit>>

    suspend fun clearBooksCache()

}