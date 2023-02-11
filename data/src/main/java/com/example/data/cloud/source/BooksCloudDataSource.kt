package com.example.data.cloud.source

import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.*
import com.example.data.models.AddBookQuestionData
import com.example.data.models.AddNewBookData
import com.example.data.models.BookData
import com.example.data.models.UpdateBookData
import kotlinx.coroutines.flow.Flow

interface BooksCloudDataSource {

    fun fetchBooks(schoolId: String, userId: String): Flow<List<BookData>>

    suspend fun fetchSavedBookById(bookId: String): CloudDataRequestState<BooksThatReadResponse>

    suspend fun fetchBookById(bookId: String): CloudDataRequestState<BookData>

    suspend fun deleteMyBook(id: String): CloudDataRequestState<Unit>

    suspend fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): CloudDataRequestState<BookQuestionResponse>

    suspend fun addBookQuestion(question: AddBookQuestionData): CloudDataRequestState<Unit>

    suspend fun addNewBook(book: AddNewBookData): CloudDataRequestState<PostRequestAnswerCloud>

    suspend fun deleteBook(id: String): CloudDataRequestState<Unit>

    suspend fun deleteBookQuestion(id: String): CloudDataRequestState<Unit>

    suspend fun updateQuestion(
        id: String,
        question: AddBookQuestionData
    ): CloudDataRequestState<Unit>

    suspend fun updateBook(id: String, book: UpdateBookData): CloudDataRequestState<Unit>
}
