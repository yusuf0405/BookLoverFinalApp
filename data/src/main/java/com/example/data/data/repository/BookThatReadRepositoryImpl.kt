package com.example.data.data.repository

import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.cache.source.BooksThatReadDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.data.mappers.toStudentBook
import com.example.data.data.models.BookThatReadData
import com.example.data.data.models.ChaptersData
import com.example.data.data.models.ProgressData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.domain.models.ProgressDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
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
                    val bookDomain = booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
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
            Status.NETWORK_ERROR -> emit(Resource.networkError())
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

    override fun updateProgress(id: String, progress: ProgressDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.updateProgress(id = id,
            progress = ProgressData(progress = progress.progress))
        if (result.status == Status.SUCCESS) {
            cacheDataSource.updateProgress(id = id, progress = progress.progress)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message))
    }

    override fun updateChapters(id: String, chapters: ChaptersDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.updateChapters(id = id,
            chapters = ChaptersData(isReadingPages = chapters.isReadingPages,
                chaptersRead = chapters.chaptersRead))
        if (result.status == Status.SUCCESS) {
            cacheDataSource.updateChapters(id = id, chapters = chapters.chaptersRead)
            cacheDataSource.updateIsReadIsPages(id = id, isReadingPages = chapters.isReadingPages)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message!!))
    }

    override suspend fun clearBooksCache() = cacheDataSource.clearTable()

    override fun fetchMyStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>> = flow {
        emit(Resource.loading())
        val response = cloudDataSource.fetchMyBooks(id = id)
        if (response.status == Status.SUCCESS) {
            val booksDataList = response.data!!
            val booksDomain =
                booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
            emit(Resource.success(data = booksDomain))
        } else emit(Resource.error(message = response.message))
    }

}















