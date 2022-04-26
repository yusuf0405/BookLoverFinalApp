package com.example.data.data.repository

import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.source.StudentBookCacheDataSource
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.source.StudentBookCloudDataSource
import com.example.data.data.mappers.toStudentBook
import com.example.data.data.models.ChaptersData
import com.example.data.data.models.ProgressData
import com.example.data.data.models.StudentBookData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.domain.models.ProgressDomain
import com.example.domain.domain.models.StudentBookDomain
import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.models.Resource
import com.example.domain.models.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StudentBooksRepositoryImpl(
    private val cloudDataSource: StudentBookCloudDataSource,
    private val cacheDataSource: StudentBookCacheDataSource,
    private val bookCashMapper: Mapper<StudentBookDb, StudentBookData>,
    private val bookDomainMapper: Mapper<StudentBookData, StudentBookDomain>,
    private val addNewBookMapper: Mapper<AddNewBookDomain, AddNewBookCloud>,
) : StudentBooksRepository {

    override fun fetchMyBooks(id: String): Flow<Resource<List<StudentBookDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchStudentBooks()
        if (booksCacheList.isEmpty()) {
            val response = cloudDataSource.fetchMyBooks(id = id)
            if (response.status == Status.SUCCESS) {
                val booksDataList = response.data!!
                cacheDataSource.saveBooks(books = booksDataList)
                val booksDomain =
                    booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
                emit(Resource.success(data = booksDomain))
            } else emit(Resource.error(message = response.message))
        } else {
            val booksData =
                booksCacheList.map { studentBookDb -> bookCashMapper.map(studentBookDb) }
            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
            emit(Resource.success(data = booksDomain))
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

    override fun getMyBook(id: String): Flow<Resource<StudentBookDomain>> = flow {
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
                createdAt = request.data!!.createdAt)
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


}















