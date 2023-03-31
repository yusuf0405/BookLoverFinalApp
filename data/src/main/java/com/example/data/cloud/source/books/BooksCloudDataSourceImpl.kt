package com.example.data.cloud.source.books

import com.example.data.base.ResponseHandler
import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.mappers.BookCloudDataMapper
import com.example.data.cloud.models.*
import com.example.data.cloud.service.BookService
import com.example.data.cloud.source.saved_books.BooksThatReadCloudDataSource
import com.example.data.models.AddBookQuestionData
import com.example.data.models.AddNewBookData
import com.example.data.models.BookData
import com.example.data.models.UpdateBookData
import com.example.domain.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksCloudDataSourceImpl @Inject constructor(
    private val service: BookService,
    private val bookCloudMapper: BookCloudDataMapper,
    private val addBookCloudMapper: Mapper<AddNewBookData, AddNewBookCloud>,
    private val updateBookCloudMapper: Mapper<UpdateBookData, UpdateBookCloud>,
    private val questionDataMapper: Mapper<AddBookQuestionData, AddBookQuestionCloud>,
    private val bookResponseToBookCloudMapper: Mapper<BookResponse, BookCloud>,
    private val savedDataSource: BooksThatReadCloudDataSource,
    private val responseHandler: ResponseHandler,
) : BooksCloudDataSource {

    override fun fetchBooks(schoolId: String, userId: String): Flow<List<BookData>> = flow {
        emit(service.fetchAllBooks(id = "{\"schoolId\":\"${schoolId}\"}"))
    }.flowOn(Dispatchers.IO)
        .map { it.body() ?: BookResponse(emptyList()) }
        .map { it.books }
        .map { books ->
            val cachedBooksId = savedDataSource.fetchUserSavedBooksId(userId)
            books.map {
                bookCloudMapper.map(it, cachedBooksId) }
        }

    override suspend fun fetchSavedBookById(bookId: String): CloudDataRequestState<BooksThatReadResponse> =
        responseHandler.safeApiCall { service.fetchSavedBooksFromId(id = "{\"bookId\":\"${bookId}\"}") }

    override suspend fun fetchBookById(bookId: String): CloudDataRequestState<BookData> =
        responseHandler.safeApiCall { service.fetchAllBooks(id = "{\"objectId\":\"${bookId}\"}") }
            .map(bookResponseToBookCloudMapper)
            .map(bookCloudMapper.map())

    override suspend fun deleteMyBook(id: String): CloudDataRequestState<Unit> =
        responseHandler.safeApiCall { service.deleteMyBook(id = id) }


    override suspend fun addNewBook(book: AddNewBookData): CloudDataRequestState<PostRequestAnswerCloud> =
        responseHandler.safeApiCall { service.addNewBook(book = addBookCloudMapper.map(book)) }

    override suspend fun deleteBook(id: String): CloudDataRequestState<Unit> =
        responseHandler.safeApiCall { service.deleteBook(id = id) }


    override suspend fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): CloudDataRequestState<BookQuestionResponse> = responseHandler.safeApiCall {
        service.getAllChapterQuestions(id = "{\"bookId\":\"$id\",\"chapter\":\"$chapter\"}")
    }

    override suspend fun addBookQuestion(question: AddBookQuestionData) =
        responseHandler.safeApiCall {
            service.addBookQuestion(question = questionDataMapper.map(question))
        }

    override suspend fun deleteBookQuestion(id: String): CloudDataRequestState<Unit> =
        responseHandler.safeApiCall { service.deleteBookQuestion(id = id) }

    override suspend fun updateQuestion(id: String, question: AddBookQuestionData) =
        responseHandler.safeApiCall {
            service.updateQuestion(
                question = questionDataMapper.map(question),
                id = id
            )
        }

    override suspend fun updateBook(id: String, book: UpdateBookData) =
        responseHandler.safeApiCall {
            service.updateBook(id = id, book = updateBookCloudMapper.map(book))
        }
}
