package com.example.data.cloud.source.saved_books

import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.AddNewBookThatReadCloud
import com.example.data.cloud.models.BookThatReadCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.models.BookThatReadData
import kotlinx.coroutines.flow.Flow

interface BooksThatReadCloudDataSource {

    fun fetchUserBooksFromIdObservable(id: String): Flow<List<BookThatReadData>>

    suspend fun fetchUserSavedBooksId(userId: String): List<String>

    fun fetchBookFromUserIdAndBookId(bookId: String, userId: String): Flow<BookThatReadCloud>

    fun fetchBooksFromByBookId(bookId: String): Flow<List<BookThatReadCloud>>

    suspend fun deleteBook(id: String): CloudDataRequestState<Unit>

    suspend fun addNewBook(book: AddNewBookThatReadCloud): CloudDataRequestState<PostRequestAnswerCloud>

    suspend fun updateProgress(id: String, progress: Int): CloudDataRequestState<Unit>

    suspend fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ): CloudDataRequestState<Unit>

}




