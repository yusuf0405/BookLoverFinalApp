package com.example.domain.repository

import com.example.domain.models.Resource
import com.example.domain.models.book.BookQuestion
import com.example.domain.models.book.BookUpdateProgress
import com.example.domain.models.book.BooksThatRead
import com.example.domain.models.student.PostRequestAnswer
import com.example.domain.models.student.UpdateAnswer
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface BookRepository {

    fun getAllChapterQuestions(id: String, chapter: String): Flow<Resource<List<BookQuestion>>>

//    fun getMyProgress(id: String): Flow<Resource<List<BooksThatRead>>>

}