package com.example.domain.domain.repository

import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.domain.models.ProgressDomain
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow


interface BookThatReadRepository {

    fun fetchMyBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>

    fun fetchMyBook(id: String): Flow<Resource<BookThatReadDomain>>

    fun deleteMyBook(id: String): Flow<Resource<Unit>>

    fun addBook(book: AddNewBookDomain): Flow<Resource<Unit>>

    fun updateProgress(id: String, progress: ProgressDomain): Flow<Resource<Unit>>

    fun updateChapters(id: String, chapters: ChaptersDomain): Flow<Resource<Unit>>

    suspend fun clearBooksCache()

    fun fetchMyStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>>


}