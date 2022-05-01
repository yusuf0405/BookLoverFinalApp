package com.example.data.data.repository

import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cloud.source.BooksCloudDataSource
import com.example.data.data.models.BookData
import com.example.data.data.models.BookQuestionData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.domain.models.BookQuestionDomain
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
    private val questionsMapper: Mapper<BookQuestionData, BookQuestionDomain>,
) : BooksRepository {

    override suspend fun fetchBooks(): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchBooks()
        if (booksCacheList.isEmpty()) {
            val result = cloudDataSource.fetchBooks()
            if (result.status == Status.SUCCESS) {
                val booksData = result.data!!
                cacheDataSource.saveBooks(books = booksData)
                val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
                emit(Resource.success(data = booksDomain))
            } else emit(Resource.error(message = result.message))
        } else {
            val booksData = booksCacheList.map { bookDb -> bookCashMapper.map(bookDb) }
            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
            emit(Resource.success(data = booksDomain))
        }
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
            if (result.data!!.isEmpty()) emit(Resource.error(message = "No questions!!"))
            else emit(Resource.success(data = result.data!!.map { questionData ->
                questionsMapper.map(questionData)
            }))
        } else emit(Resource.error(message = result.message!!))
    }

}
