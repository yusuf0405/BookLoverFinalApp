package com.example.data.data.repository

import com.example.data.data.cache.models.BookDb
import com.example.data.data.cache.source.BooksCacheDataSource
import com.example.data.data.cloud.models.BookCloud
import com.example.data.data.cloud.source.BooksCloudDataSource
import com.example.data.data.models.BookData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class BooksRepositoryImpl(
    private val cloudDataSource: BooksCloudDataSource,
    private val cacheDataSource: BooksCacheDataSource,
    private val bookCloudMapper: Mapper<BookCloud, BookData>,
    private val bookCashMapper: Mapper<BookDb, BookData>,
    private val bookDomainMapper: Mapper<BookData, BookDomain>,
) : BooksRepository {

    override suspend fun fetchBooks(): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchBooks()
        if (booksCacheList.isEmpty()) {
            val result = cloudDataSource.fetchBooks()
            if (result.status == Status.SUCCESS) {
                val booksCloudList = result.data!!.books
                val booksData = booksCloudList.map { bookCloud -> bookCloudMapper.map(bookCloud) }
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
        emit(Resource.loading())
        val urlConnection = URL(url).openConnection() as HttpsURLConnection
        if (urlConnection.responseCode == 200) emit(Resource.success(data = BufferedInputStream(
            urlConnection.inputStream)))
        else emit(Resource.error(message = urlConnection.responseMessage))

    }
}
