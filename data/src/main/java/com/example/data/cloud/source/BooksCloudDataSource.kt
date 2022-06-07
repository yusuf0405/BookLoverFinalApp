package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.models.*
import com.example.data.cloud.service.BookService
import com.example.data.models.*
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status

interface BooksCloudDataSource {

    suspend fun fetchBooks(schoolId: String): Resource<List<BookData>>

    suspend fun fetchMyBooks(bookId: String): Resource<List<BookThatReadCloud>>

    suspend fun deleteMyBook(id: String): Resource<Unit>

    suspend fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): Resource<List<BookQuestionData>>

    suspend fun addBookQuestion(question: AddBookQuestionData): Resource<Unit>

    suspend fun addNewBook(book: AddNewBookData): Resource<PostRequestAnswerCloud>

    suspend fun deleteBook(id: String): Resource<Unit>

    suspend fun deleteBookQuestion(id: String): Resource<Unit>

    suspend fun updateQuestion(id: String, question: AddBookQuestionData): Resource<Unit>

    suspend fun updateBook(id: String, book: UpdateBookData): Resource<Unit>

    class Base(
        private val service: BookService,
        private val bookCloudMapper: Mapper<BookCloud, BookData>,
        private val addBookCloudMapper: Mapper<AddNewBookData, AddNewBookCloud>,
        private val updateBookCloudMapper: Mapper<UpdateBookData, UpdateBookCloud>,
        private val questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
        private val questionDataMapper: Mapper<AddBookQuestionData, AddBookQuestionCloud>,
        resourceProvider: ResourceProvider,
    ) : BooksCloudDataSource, BaseApiResponse(resourceProvider = resourceProvider) {

        override suspend fun fetchBooks(schoolId: String): Resource<List<BookData>> {
            val response =
                safeApiCall() { service.fetchAllBooks(id = "{\"schoolId\":\"${schoolId}\"}") }
            return if (response.status == Status.SUCCESS) {
                val bookData =
                    response.data!!.books.map { bookCloud -> bookCloudMapper.map(bookCloud) }
                Resource.success(data = bookData)
            } else Resource.error(message = response.message!!)

        }

        override suspend fun fetchMyBooks(bookId: String): Resource<List<BookThatReadCloud>> {
            val response = safeApiCall { service.fetchMyBooks(id = "{\"bookId\":\"${bookId}\"}") }
            return if (response.status == Status.SUCCESS) {
                Resource.success(data = response.data!!.books)
            } else Resource.error(message = response.message!!)
        }

        override suspend fun deleteMyBook(id: String): Resource<Unit> =
            safeApiCall { service.deleteMyBook(id = id) }


        override suspend fun addNewBook(book: AddNewBookData): Resource<PostRequestAnswerCloud> =
            safeApiCall { service.addNewBook(book = addBookCloudMapper.map(book)) }

        override suspend fun deleteBook(id: String): Resource<Unit> =
            safeApiCall { service.deleteBook(id = id) }


        override suspend fun fetchChapterQuestions(
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

        override suspend fun updateBook(id: String, book: UpdateBookData): Resource<Unit> =
            safeApiCall { service.updateBook(id = id, book = updateBookCloudMapper.map(book)) }

    }
}
