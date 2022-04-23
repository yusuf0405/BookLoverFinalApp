package com.example.domain.repository

import com.example.domain.models.Resource
import com.example.domain.models.book.*
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.models.student.UpdateAnswer
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BookRepository {

    fun getBookForReading(url: String): Flow<Resource<InputStream>>

    fun addNewStudentBook(
        book: AddNewBookRequest,
    ): Flow<Resource<PostRequestAnswer>>


    fun getMyBook(userId: String, bookId: String): Flow<Resource<List<AddNewBookRequest>>>

    fun updateProgressStudentBook(
        id: String,
        progress: BookUpdateProgress,
    ): Flow<Resource<UpdateAnswer>>

    fun getAllChapterQuestions(id: String, chapter: String): Flow<Resource<List<BookQuestion>>>

    fun getMyProgress(id: String): Flow<Resource<List<BooksThatRead>>>

}