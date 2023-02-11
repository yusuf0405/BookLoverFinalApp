package com.example.data.repository

import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.SavedStatusCache
import com.example.data.cache.source.books.BooksCacheDataSource
import com.example.data.cache.source.books_that_read.BooksThatReadCacheDataSource
import com.example.data.cloud.models.AddNewBookThatReadCloud
import com.example.data.cloud.source.BooksThatReadCloudDataSource
import com.example.data.mappers.toStudentBook
import com.example.data.models.BookThatReadData
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.Resource
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BookThatReadRepositoryImpl @Inject constructor(
    private val cloudDataSource: BooksThatReadCloudDataSource,
    private val cacheDataSource: BooksThatReadCacheDataSource,
    private val bookCacheDataSource: BooksCacheDataSource,
    private val userStatisticsRepository: UserStatisticsRepository,
    private val bookCashMapper: Mapper<BookThatReadCache, BookThatReadData>,
    private val bookDomainMapper: Mapper<BookThatReadData, BookThatReadDomain>,
    private val addNewBookMapper: Mapper<AddNewBookThatReadDomain, AddNewBookThatReadCloud>,
) : BookThatReadRepository, BaseRepository {

    override fun fetchUserAllBooksThatReadByUserId(id: String): Flow<List<BookThatReadDomain>> =
        flow { emit(cacheDataSource.fetchBooksThatReadSingle()) }
            .flatMapLatest { cachedBooks -> handleCachedBook(cachedBooks, id) }
            .map(::mapBookDataList)
            .flowOn(Dispatchers.Default)

    private fun handleCachedBook(cachedBooks: List<BookThatReadCache>, userId: String) =
        if (cachedBooks.isEmpty()) cloudDataSource.fetchUserBooksFromIdObservable(id = userId)
            .flowOn(Dispatchers.IO)
            .onEach(cacheDataSource::saveBooks)
        else cacheDataSource.fetchBooksThatReadObservable()
            .map(::mapBookCacheList)

    private fun mapBookCacheList(books: List<BookThatReadCache>) = books.map(bookCashMapper::map)

    private fun mapBookDataList(books: List<BookThatReadData>) = books.map(bookDomainMapper::map)

    override fun fetchUserAllBooksThatReadFromCloud(userId: String): Flow<List<BookThatReadDomain>> =
        cloudDataSource
            .fetchUserBooksFromIdObservable(id = userId)
            .map { savedBooks -> savedBooks.map(bookDomainMapper::map) }

    override suspend fun fetchSavedBookByBookIdFromCache(bookId: String): BookThatReadDomain {
        return bookDomainMapper.map(
            bookCashMapper.map(
                cacheDataSource.fetchBooksByBookId(bookId = bookId) ?: BookThatReadCache.unknown()
            )
        )
    }


    override fun fetchStudentBooks(id: String): Flow<Resource<List<BookThatReadDomain>>> = flow {
//        emit(Resource.loading())
//        val response = cloudDataSource.fetchUserBookFromId(id = id)
//        if (response.status == Status.SUCCESS) {
//            val booksDataList = response.data!!
//            if (booksDataList.isEmpty()) emit(Resource.empty())
//            else {
//                val bookDomain =
//                    booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
//                emit(Resource.success(data = bookDomain))
//            }
//        } else emit(Resource.error(message = response.message))
    }


    override suspend fun deleteBookInSavedBooks(id: String): RequestState<Unit> {
        val bookCache = cacheDataSource.fetchBooksByBookId(id) ?: BookThatReadCache.unknown()
        return renderResult(
            result = cloudDataSource.deleteBook(id = bookCache.objectId),
            onSuccess = {
                cacheDataSource.deleteBook(id = bookCache.objectId)
                bookCacheDataSource.updateBookSavedStatus(SavedStatusCache.NOT_SAVED, id)
            }
        )
    }

    override suspend fun addBookToSavedBooks(book: AddNewBookThatReadDomain) = renderResultToUnit(
        result = cloudDataSource.addNewBook(addNewBookMapper.map(book)),
        onSuccess = { data ->
            val studentBook = book.toStudentBook(
                objectId = data.objectId,
                createdAt = data.createdAt,
                path = book.book
            )
            bookCacheDataSource.updateBookSavedStatus(SavedStatusCache.SAVED, book.bookId)
            cacheDataSource.addBook(book = studentBook)
        }
    )


    override suspend fun updateProgress(
        id: String,
        progress: Int,
        currentDayProgress: Int
    ): RequestState<Unit> = renderResult(
        result = cloudDataSource.updateProgress(id = id, progress = progress),
        onSuccess = {
            userStatisticsRepository.saveNewDayToStatistics(progress = currentDayProgress)
            cacheDataSource.updateProgress(id = id, progress = progress)
        }
    )

    override suspend fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
    ) = renderResult(
        result = cloudDataSource.updateChapters(
            id = id,
            chapters = chapters,
            isReadingPages = isReadingPages
        ),
        onSuccess = {
            cacheDataSource.updateChapters(id = id, chapters = chapters)
            cacheDataSource.updateIsReadIsPages(id = id, isReadingPages = isReadingPages)
        }
    )

    override fun fetchUsersBooks(id: String): Flow<Resource<List<BookThatReadDomain>>> = flow {
//        emit(Resource.loading())
//        val response = cloudDataSource.fetchUserBookFromId(id = id)
//        if (response.status == Status.SUCCESS) {
//            val booksDataList = response.data!!
//            val booksDomain =
//                booksDataList.map { studentBookData -> bookDomainMapper.map(studentBookData) }
//            emit(Resource.success(data = booksDomain))
//        } else emit(Resource.error(message = response.message))
    }

    override suspend fun clearBooksCache() = cacheDataSource.clearTable()

}















