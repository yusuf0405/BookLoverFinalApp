package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.AddBookQuestionDomain
import com.example.domain.models.BookDomain
import com.example.domain.models.BookQuestionDomain
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BooksRepository {

    fun fetchBooks(schoolId: String): Flow<Resource<List<BookDomain>>>

    fun getBookForReading(url: String): Flow<Resource<InputStream>>

    fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestionDomain>>>

    fun addBookQuestion(question: AddBookQuestionDomain): Flow<Resource<Unit>>

    fun deleteBookQuestion(id: String): Flow<Resource<Unit>>

    fun updateBookQuestion(id: String, question: AddBookQuestionDomain): Flow<Resource<Unit>>

    suspend fun clearBooksCache()

}