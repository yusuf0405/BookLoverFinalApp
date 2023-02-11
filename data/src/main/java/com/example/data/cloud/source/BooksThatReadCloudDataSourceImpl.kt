package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.mappers.BookMapper
import com.example.data.cloud.mappers.BookThatReadMapper
import com.example.data.cloud.models.*
import com.example.data.cloud.service.BookThatReadService
import com.example.data.models.BookThatReadData
import com.example.domain.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BooksThatReadCloudDataSourceImpl @Inject constructor(
    private val thatReadService: BookThatReadService,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
) : BooksThatReadCloudDataSource, BaseApiResponse(resourceProvider = resourceProvider) {
    override fun fetchUserBooksFromIdObservable(id: String): Flow<List<BookThatReadData>> =
        flow {
            val bookList = mutableListOf<BookThatReadData>()
            val booksThatReadCloud = thatReadService.fetchMyBooks(id = "{\"userId\":\"${id}\"}")
            booksThatReadCloud.body()!!.books.forEach { booksThatRead ->
                val response =
                    thatReadService.getBook(id = "{\"objectId\":\"${booksThatRead.bookId}\"}")
                val bookCloudList = response.body()!!.books
                if (bookCloudList.isNotEmpty()) {
                    val bookCloud = bookCloudList[0]
                    val thatReadBook = BookThatReadMapper.Base(
                        progress = booksThatRead.progress,
                        objectId = booksThatRead.objectId,
                        createdAt = booksThatRead.createdAt,
                        chaptersRead = booksThatRead.chaptersRead,
                        bookId = booksThatRead.bookId,
                        studentId = booksThatRead.studentId,
                        isReadingPages = booksThatRead.isReadingPages,
                        path = booksThatRead.path
                    )

                    val book = BookMapper.Base(
                        author = bookCloud.author,
                        id = bookCloud.id,
                        poster = bookCloud.poster,
                        page = bookCloud.page,
                        chapterCount = bookCloud.chapterCount,
                        publicYear = bookCloud.publicYear,
                        title = bookCloud.title,
                        updatedAt = bookCloud.updatedAt
                    )

                    bookList.add(thatReadBook.map(BookMapper.ComplexMapper(book)))
                }
            }
            emit(bookList)
        }

    override suspend fun fetchUserSavedBooksId(userId: String): List<String> {
        val booksThatReadCloud = thatReadService.fetchMyBooks(id = "{\"userId\":\"${userId}\"}")
        return if (booksThatReadCloud.body() != null) booksThatReadCloud.body()!!.books.map { it.bookId }
        else emptyList()
    }

    override fun fetchBookFromUserIdAndBookId(
        bookId: String,
        userId: String
    ): Flow<BookThatReadCloud> = flow {
        emit(thatReadService.fetchBookFromUserIdAndBookId(id = "{\"bookId\":\"$bookId\",\"userId\":\"$userId\"}"))
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: BooksThatReadResponse(emptyList()) }
        .map { it.books.first() }
        .flowOn(dispatchersProvider.default())

    override fun fetchBooksFromByBookId(bookId: String): Flow<List<BookThatReadCloud>> = flow {
        emit(thatReadService.fetchBookFromUserIdAndBookId(id = "{\"bookId\":\"$bookId\"}"))
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: BooksThatReadResponse(emptyList()) }
        .map { it.books }
        .flowOn(dispatchersProvider.default())

    override suspend fun deleteBook(id: String) =
        safeApiCalll { thatReadService.deleteMyBook(id = id) }

    override suspend fun addNewBook(book: AddNewBookThatReadCloud) =
        safeApiCalll { thatReadService.addNewBookStudent(book = book) }

    override suspend fun updateProgress(id: String, progress: Int) = safeApiCalll {
        thatReadService.bookThatReadUpdateProgress(
            id = id,
            progress = UpdateProgressCloud(progress = progress)
        )
    }

    override suspend fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ) = safeApiCalll {
        thatReadService.bookThatReadUpdatePages(
            id = id,
            chapters = UpdateChaptersCloud(
                chaptersRead = chapters,
                isReadingPages = isReadingPages
            )
        )
    }
}