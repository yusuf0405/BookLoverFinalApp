package com.example.data.data.repository

import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.cache.source.StudentBookCacheDataSource
import com.example.data.data.cloud.source.StudentBookCloudDataSource
import com.example.data.data.models.StudentBookData
import com.example.domain.domain.Mapper
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
) : StudentBooksRepository {

    override suspend fun fetchMyBooks(id: String): Flow<Resource<List<StudentBookDomain>>> = flow {
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

            }
            else emit(Resource.error(message = response.message))

        }
        else {
            val booksData =
                booksCacheList.map { studentBookDb -> bookCashMapper.map(studentBookDb) }

            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }

            emit(Resource.success(data = booksDomain))
        }
    }


    override suspend fun deleteMyBook(id: String): Flow<Resource<Unit>> = flow {
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

}