package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.models.AddBookQuestionCloud
import com.example.data.cloud.models.BookCloud
import com.example.data.cloud.models.BookQuestionCloud
import com.example.data.cloud.service.BookService
import com.example.data.models.AddBookQuestionData
import com.example.data.models.BookData
import com.example.data.models.BookQuestionData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status

interface BooksCloudDataSource {

    suspend fun fetchBooks(schoolId: String): Resource<List<BookData>>

    suspend fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Resource<List<BookQuestionData>>

    suspend fun addBookQuestion(question: AddBookQuestionData): Resource<Unit>

    suspend fun deleteBookQuestion(id: String): Resource<Unit>

    suspend fun updateQuestion(id: String, question: AddBookQuestionData): Resource<Unit>

    class Base(
        private val service: BookService,
        private val bookCloudMapper: Mapper<BookCloud, BookData>,
        private val questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
        private val questionDataMapper: Mapper<AddBookQuestionData, AddBookQuestionCloud>,
        resourceProvider: ResourceProvider,
    ) : BooksCloudDataSource, BaseApiResponse(resourceProvider = resourceProvider) {

        override suspend fun fetchBooks(schoolId: String): Resource<List<BookData>> {
            val response =
                safeApiCall() { service.fetchAllBooks() }
            return if (response.status == Status.SUCCESS) {
                val bookData =
                    response.data!!.books.map { bookCloud -> bookCloudMapper.map(bookCloud) }
                Resource.success(data = bookData)
            } else Resource.error(message = response.message!!)

        }

        override suspend fun getAllChapterQuestions(
            id: String,
            chapter: String,
        ): Resource<List<BookQuestionData>> {
            val response =
                safeApiCall { service.getAllChapterQuestions(id = "{\"bookId\":\"$id\",\"chapter\":\"$chapter\"}") }
            return if (response.status == Status.SUCCESS) {
                val bookData =
                    response.data!!.questions.map { questionCloud ->
                        questionCloudMapper.map(questionCloud)
                    }
                Resource.success(data = bookData)
            } else Resource.error(message = response.message!!)

        }


        override suspend fun addBookQuestion(question: AddBookQuestionData): Resource<Unit> =
            safeApiCall { service.addBookQuestion(question = questionDataMapper.map(question)) }

        override suspend fun deleteBookQuestion(id: String): Resource<Unit> =
            safeApiCall { service.deleteBookQuestion(id = id) }

        override suspend fun updateQuestion(id: String, question: AddBookQuestionData) =
            safeApiCall {
                service.updateQuestion(question = questionDataMapper.map(question),
                    id = id)
            }

    }
}
