package com.example.data.repository

import com.example.data.cache.models.BookDb
import com.example.data.cache.source.BooksCacheDataSource
import com.example.data.cloud.source.BooksCloudDataSource
import com.example.data.models.AddBookQuestionData
import com.example.data.models.AddNewBookData
import com.example.data.models.BookData
import com.example.data.models.BookQuestionData
import com.example.data.toBook
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.AddBookQuestionDomain
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.BookDomain
import com.example.domain.models.BookQuestionDomain
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class BooksRepositoryImpl(
    private val cloudDataSource: BooksCloudDataSource,
    private val cacheDataSource: BooksCacheDataSource,
    private val bookCashMapper: Mapper<BookDb, BookData>,
    private val bookDomainMapper: Mapper<BookData, BookDomain>,
    private val addBookDomainMapper: Mapper<AddNewBookDomain, AddNewBookData>,
    private val questionsMapper: Mapper<BookQuestionData, BookQuestionDomain>,
    private val questionsDomainMapper: Mapper<AddBookQuestionDomain, AddBookQuestionData>,
) : BooksRepository {

    override fun fetchBooks(schoolId: String): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchBooks()
        if (booksCacheList.isEmpty()) {
            val result = cloudDataSource.fetchBooks(schoolId = schoolId)
            if (result.status == Status.SUCCESS) {
                val booksData = result.data!!
                if (booksData.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveBooks(books = booksData)
                    val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
                    emit(Resource.success(data = booksDomain))
                }
            } else emit(Resource.error(message = result.message))
        } else {
            val booksData = booksCacheList.map { bookDb -> bookCashMapper.map(bookDb) }
            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
            if (booksDomain.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = booksDomain))
        }
    }

    override fun addNewBook(book: AddNewBookDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addNewBook(book = addBookDomainMapper.map(book))
        if (result.status == Status.SUCCESS) {
            cacheDataSource.addNewBook(book = book.toBook(id = result.data!!.objectId,
                createdAt = result.data!!.createdAt))
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message!!))
    }

    override fun getBookForReading(url: String): Flow<Resource<InputStream>> = flow {
        try {
            emit(Resource.loading())
            val urlConnection = URL(url).openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == 200) {
                emit(Resource.success(BufferedInputStream(urlConnection.inputStream)))
            } else emit(Resource.error(message = urlConnection.responseMessage))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message))
        }

    }

    override fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestionDomain>>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.getAllChapterQuestions(id = id, chapter = chapter)
        if (result.status == Status.SUCCESS) {
            if (result.data!!.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = result.data!!.map { questionData ->
                questionsMapper.map(questionData)
            }))
        } else emit(Resource.error(message = result.message!!))
    }

    override fun addBookQuestion(question: AddBookQuestionDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addBookQuestion(question = questionsDomainMapper.map(question))
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))
    }

    override fun deleteBookQuestion(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteBookQuestion(id = id)
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))
    }

    override fun updateBookQuestion(
        id: String,
        question: AddBookQuestionDomain,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result =
            cloudDataSource.updateQuestion(id = id, question = questionsDomainMapper.map(question))
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))

    }

    override suspend fun clearBooksCache() = cacheDataSource.clearTable()

}
