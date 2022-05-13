package com.example.domain.repository

import com.example.domain.Resource
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookThatReadDomain
import kotlinx.coroutines.flow.Flow


interface BookThatReadRepository {

    fun fetchMyBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>

    fun fetchMyBook(id: String): Flow<Resource<BookThatReadDomain>>

    fun deleteMyBook(id: String): Flow<Resource<Unit>>

    fun addBook(book: AddNewBookThatReadDomain): Flow<Resource<Unit>>

    fun updateProgress(id: String, progress: Int): Flow<Resource<Unit>>

    fun updateChapters(id: String, chapters: Int, isReadingPages: List<Boolean>): Flow<Resource<Unit>>

    fun fetchMyStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>

    suspend fun clearBooksCache()

}