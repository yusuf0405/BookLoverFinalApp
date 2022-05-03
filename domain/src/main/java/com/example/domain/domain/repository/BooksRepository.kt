package com.example.domain.domain.repository

import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.models.BookQuestionDomain
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BooksRepository {

    suspend fun fetchBooks(): Flow<Resource<List<BookDomain>>>

    fun getBookForReading(url: String): Flow<Resource<InputStream>>

    fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestionDomain>>>

    suspend fun clearBooksCache()

}