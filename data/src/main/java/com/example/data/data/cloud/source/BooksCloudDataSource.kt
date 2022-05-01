package com.example.data.data.cloud.source

import com.example.data.data.base.BaseApiResponse
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.cloud.service.BookService
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.domain.domain.Mapper
import com.example.domain.models.Resource
import com.example.domain.models.Status

interface BooksCloudDataSource {

    suspend fun fetchBooks(): Resource<List<BookData>>

    suspend fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Resource<List<BookQuestionData>>

    class Base(
        private val service: BookService,
        private val bookCloudMapper: Mapper<BookCloud, BookData>,
        private val questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
    ) : BooksCloudDataSource, BaseApiResponse() {

        override suspend fun fetchBooks(): Resource<List<BookData>> {
            val response = safeApiCall { service.fetchAllBooks() }
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
    }
}
