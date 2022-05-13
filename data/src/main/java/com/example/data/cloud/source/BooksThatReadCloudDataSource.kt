package com.example.data.cloud.source

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.mappers.BookMapper
import com.example.data.cloud.mappers.BookThatReadMapper
import com.example.data.cloud.models.AddNewBookThatReadCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.cloud.models.UpdateChaptersCloud
import com.example.data.cloud.models.UpdateProgressCloud
import com.example.data.cloud.service.BookThatReadService
import com.example.data.models.BookThatReadData
import com.example.domain.Resource

interface BooksThatReadCloudDataSource {

    suspend fun fetchMyBooks(id: String): Resource<List<BookThatReadData>>

    suspend fun deleteBook(id: String): Resource<Unit>

    suspend fun addNewBook(book: AddNewBookThatReadCloud): Resource<PostRequestAnswerCloud>

    suspend fun updateProgress(id: String, progress: Int): Resource<Unit>

    suspend fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ): Resource<Unit>

    class Base(
        private val thatReadService: BookThatReadService,
        private val resourceProvider: ResourceProvider,
    ) : BooksThatReadCloudDataSource,
        BaseApiResponse(resourceProvider = resourceProvider) {

        override suspend fun fetchMyBooks(id: String): Resource<List<BookThatReadData>> = try {
            val bookList = mutableListOf<BookThatReadData>()

            val booksThatReadCloud = thatReadService.fetchMyBooks(id = "{\"userId\":\"${id}\"}")

            booksThatReadCloud.body()!!.books.forEach { booksThatRead ->

                val response =
                    thatReadService.getBook(id = "{\"objectId\":\"${booksThatRead.bookId}\"}")

                val bookCloud = response.body()!!.books[0]


                val thatReadBook = BookThatReadMapper.Base(
                    progress = booksThatRead.progress,
                    objectId = booksThatRead.objectId,
                    createdAt = booksThatRead.createdAt,
                    chaptersRead = booksThatRead.chaptersRead,
                    bookId = booksThatRead.bookId,
                    studentId = booksThatRead.studentId,
                    isReadingPages = booksThatRead.isReadingPages,
                    path = booksThatRead.path)

                val book = BookMapper.Base(author = bookCloud.author,
                    id = bookCloud.id,
                    poster = bookCloud.poster,
                    page = bookCloud.page,
                    chapterCount = bookCloud.chapterCount,
                    publicYear = bookCloud.publicYear,
                    title = bookCloud.title,
                    updatedAt = bookCloud.updatedAt)

                bookList.add(thatReadBook.map(BookMapper.ComplexMapper(book)))
            }
            Resource.success(bookList)
        } catch (exception: Exception) {
            Resource.error(message = resourceProvider.errorType(exception = exception))

        }

        override suspend fun deleteBook(id: String) =
            safeApiCall { thatReadService.deleteMyBook(id = id) }

        override suspend fun addNewBook(book: AddNewBookThatReadCloud): Resource<PostRequestAnswerCloud> =
            safeApiCall { thatReadService.addNewBookStudent(book = book) }

        override suspend fun updateProgress(id: String, progress: Int): Resource<Unit> =
            safeApiCall {
                thatReadService.bookThatReadUpdateProgress(id = id,
                    progress = UpdateProgressCloud(progress = progress))
            }


        override suspend fun updateChapters(
            id: String,
            chapters: Int,
            isReadingPages: List<Boolean>,
        ): Resource<Unit> =
            safeApiCall {
                thatReadService.bookThatReadUpdatePages(id = id,
                    chapters = UpdateChaptersCloud(chaptersRead = chapters,
                        isReadingPages = isReadingPages))
            }


    }
}



