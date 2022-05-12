package com.example.data.repository

import com.example.data.cache.models.BookThatReadDb
import com.example.data.cache.source.BooksThatReadDataSource
import com.example.data.cloud.models.AddNewBookCloud
import com.example.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.mappers.toStudentBook
import com.example.data.models.BookThatReadData
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookThatReadRepositoryImpl(
    private val cloudDataSource: BooksThatReadCloudDataSource,
    private val cacheDataSource: BooksThatReadDataSource,
    private val bookCashMapper: Mapper<BookThatReadDb, BookThatReadData>,
    private val bookDomainMapper: Mapper<BookThatReadData, BookThatReadDomain>,
    private val addNewBookMapper: Mapper<AddNewBookDomain, AddNewBookCloud>,
) : BookThatReadRepository {

    override fun fetchMyBooks(id: String): Flow<Resource<List<BookThatReadDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchBooksThatRead()
        if (booksCacheList.isEmpty()) {
            val response = cloudDataSource.fetchMyBooks(id = id)
            if (response.status == Status.SUCCESS) {
                val booksDataList = response.data!!
                if (booksDataList.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveBooks(books = booksDataList)
                    val bookDomain =
                        booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
                    emit(Resource.success(data = bookDomain))
                }
            } else emit(Resource.error(message = response.message))
        } else {
            val booksData = booksCacheList.map { bookDb -> bookCashMapper.map(bookDb) }
            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
            if (booksDomain.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = booksDomain))
        }
    }


    override fun deleteMyBook(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteBook(id = id)
        when (result.status) {
            Status.SUCCESS -> {
                cacheDataSource.deleteBook(id = id)
                emit(Resource.success(data = result.data!!))
            }
            Status.ERROR -> emit(Resource.error(message = result.message))
            Status.LOADING -> TODO()
        }
    }

    override fun fetchMyBook(id: String): Flow<Resource<BookThatReadDomain>> = flow {
        val result = cacheDataSource.getMyBook(id = id)
        if (result == null) emit(Resource.error(message = null))
        else {
            val bookData = bookCashMapper.map(result)
            emit(Resource.success(bookDomainMapper.map(bookData)))
        }
    }


    override fun addBook(book: AddNewBookDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val request = cloudDataSource.addNewBook(addNewBookMapper.map(book))
        if (request.status == Status.SUCCESS) {
            val studentBook = book.toStudentBook(objectId = request.data!!.objectId,
                createdAt = request.data!!.createdAt,
                path = book.book)
            cacheDataSource.addBook(book = studentBook)
            emit(Resource.success(Unit))
        } else emit(Resource.error(message = request.message!!))
    }

    override fun updateProgress(id: String, progress: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.updateProgress(id = id,
            progress = progress)
        if (result.status == Status.SUCCESS) {
            cacheDataSource.updateProgress(id = id, progress = progress)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message))
    }

    override fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.updateChapters(id = id,
            chapters = chapters,
            isReadingPages = isReadingPages)
        if (result.status == Status.SUCCESS) {
            cacheDataSource.updateChapters(id = id, chapters = chapters)
            cacheDataSource.updateIsReadIsPages(id = id, isReadingPages = isReadingPages)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message!!))
    }


    override fun fetchMyStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>> = flow {
        emit(Resource.loading())
        val response = cloudDataSource.fetchMyBooks(id = id)
        if (response.status == Status.SUCCESS) {
            val booksDataList = response.data!!
            val booksDomain =
                booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
            if (booksDomain.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = booksDomain))
        } else emit(Resource.error(message = response.message))
    }

    override suspend fun clearBooksCache() = cacheDataSource.clearTable()

}















