package com.example.data.data.cloud.source

import com.example.data.R
import com.example.data.data.ResourceProvider
import com.example.data.data.base.BaseApiResponse
import com.example.data.data.cloud.mappers.BookMapper
import com.example.data.data.cloud.mappers.BookThatReadMapper
import com.example.data.data.cloud.models.AddNewBookCloud
import com.example.data.data.cloud.models.PostRequestAnswerCloud
import com.example.data.data.cloud.models.UpdateChaptersCloud
import com.example.data.data.cloud.models.UpdateProgressCloud
import com.example.data.data.cloud.service.BookThatReadService
import com.example.data.data.models.BookThatReadData
import com.example.data.data.models.ChaptersData
import com.example.data.data.models.ProgressData
import com.example.domain.models.Resource
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface BooksThatReadCloudDataSource {

    suspend fun fetchMyBooks(id: String): Resource<List<BookThatReadData>>

    suspend fun deleteBook(id: String): Resource<Unit>

    suspend fun addNewBook(book: AddNewBookCloud): Resource<PostRequestAnswerCloud>

    suspend fun updateProgress(id: String, progress: ProgressData): Resource<Unit>


    suspend fun updateChapters(id: String, chapters: ChaptersData): Resource<Unit>

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
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is SocketTimeoutException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is ConnectException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is HttpException -> Resource.error(resourceProvider.getString(R.string.service_unavailable))
                else -> Resource.error(resourceProvider.getString(R.string.generic_error))
            }
        }


        override suspend fun deleteBook(id: String) =
            safeApiCall { thatReadService.deleteMyBook(id = id) }


        override suspend fun addNewBook(book: AddNewBookCloud): Resource<PostRequestAnswerCloud> =
            safeApiCall { thatReadService.addNewBookStudent(book = book) }

        override suspend fun updateProgress(id: String, progress: ProgressData): Resource<Unit> =
            safeApiCall {
                thatReadService.bookThatReadUpdateProgress(id = id,
                    progress = UpdateProgressCloud(progress = progress.progress))
            }


        override suspend fun updateChapters(id: String, chapters: ChaptersData): Resource<Unit> =
            safeApiCall {
                thatReadService.bookThatReadUpdatePages(id = id,
                    chapters = UpdateChaptersCloud(chaptersRead = chapters.chaptersRead,
                        isReadingPages = chapters.isReadingPages))
            }


    }
}



