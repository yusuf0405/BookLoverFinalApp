package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.Resource
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookThatReadDomain
import kotlinx.coroutines.flow.Flow


interface BookThatReadRepository {

    fun fetchUserAllBooksThatReadByUserId(id: String): Flow<List<BookThatReadDomain>>

    fun fetchUserAllBooksThatReadFromCloud(userId: String): Flow<List<BookThatReadDomain>>

    suspend fun fetchSavedBookByBookIdFromCache(bookId: String): BookThatReadDomain

    fun fetchStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>

    suspend fun deleteBookInSavedBooks(id: String): RequestState<Unit>

    suspend fun addBookToSavedBooks(book: AddNewBookThatReadDomain): RequestState<Unit>

    suspend fun updateProgress(
        id: String,
        progress: Int,
        currentDayProgress: Int
    ): RequestState<Unit>

    suspend fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ): RequestState<Unit>

    fun fetchUsersBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>

    suspend fun clearBooksCache()

}