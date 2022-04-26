package com.example.domain.domain.repository

import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.domain.models.ProgressDomain
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow


interface StudentBooksRepository {

    fun fetchMyBooks(id: String): Flow<Resource<List<StudentBookDomain>>>

    fun deleteMyBook(id: String): Flow<Resource<Unit>>

    fun getMyBook(id: String): Flow<Resource<StudentBookDomain>>

    fun addBook(book: AddNewBookDomain): Flow<Resource<Unit>>

    fun updateProgress(id: String, progress: ProgressDomain): Flow<Resource<Unit>>

    fun updateChapters(id: String, chapters: ChaptersDomain): Flow<Resource<Unit>>

}