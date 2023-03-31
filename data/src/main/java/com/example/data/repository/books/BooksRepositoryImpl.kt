package com.example.data.repository.books

import android.util.Log
import com.example.data.cache.models.BookCache
import com.example.data.cache.source.books.BooksCacheDataSource
import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.BookQuestionCloud
import com.example.data.cloud.source.books.BooksCloudDataSource
import com.example.data.cloud.takeSuccess
import com.example.data.models.*
import com.example.data.mapToBook
import com.example.data.repository.BaseRepository
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.RequestState
import com.example.domain.models.*
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.flow.*
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class BooksRepositoryImpl @Inject constructor(
    private val cloudDataSource: BooksCloudDataSource,
    private val cacheDataSource: BooksCacheDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val bookCashMapper: Mapper<BookCache, BookData>,
    private val bookDomainMapper: Mapper<BookData, BookDomain>,
    private val updateBookDomainMapper: Mapper<UpdateBookDomain, UpdateBookData>,
    private val addBookDomainMapper: Mapper<AddNewBookDomain, AddNewBookData>,
    private val questionsMapper: Mapper<BookQuestionData, BookQuestionDomain>,
    private val questionCloudMapper: Mapper<BookQuestionCloud, BookQuestionData>,
    private val questionsDomainMapper: Mapper<AddBookQuestionDomain, AddBookQuestionData>,
) : BooksRepository, BaseRepository {

    override fun fetchAllBooks(schoolId: String, userId: String): Flow<List<BookDomain>> =
        flow { emit(cacheDataSource.fetchAllBooksSingle()) }
            .flatMapLatest { handleCachedBook(it, schoolId, userId) }
            .flowOn(dispatchersProvider.io())
            .map(::mapBookDataList)
            .flowOn(dispatchersProvider.default())

    override fun fetchBookObservable(bookId: String): Flow<BookDomain> =
        cacheDataSource.fetchBookObservable(bookId = bookId).map { bookFromCache ->
            if (bookFromCache == null) cloudDataSource.fetchBookById(bookId).takeSuccess()
            else bookCashMapper.map(bookFromCache)
        }.map { it ?: BookData.unknown }
            .map(bookDomainMapper::map)
            .flowOn(dispatchersProvider.default())

    override fun fetchBooksFromCache(): Flow<List<BookDomain>> =
        cacheDataSource.fetchAllBooksObservable()
            .flowOn(dispatchersProvider.io())
            .map { books -> books.map(bookCashMapper::map) }
            .map { books -> books.map(bookDomainMapper::map) }
            .flowOn(dispatchersProvider.default())

    override suspend fun fetchBookFromIdInCache(bookId: String): BookDomain =
        bookDomainMapper.map(bookCashMapper.map(cacheDataSource.fetchBookFromId(bookId = bookId)))

    private fun handleCachedBook(cachedBooks: List<BookCache>, schoolId: String, userId: String) =
        if (cachedBooks.isEmpty()) cloudDataSource.fetchBooks(schoolId = schoolId, userId = userId)
            .onEach(cacheDataSource::saveBooks)
            .flowOn(dispatchersProvider.io())
        else cacheDataSource.fetchAllBooksObservable()
            .flowOn(dispatchersProvider.io())
            .map(::mapBookCacheList)
            .flowOn(dispatchersProvider.default())

    private fun mapBookCacheList(books: List<BookCache>) = books.map(bookCashMapper::map)

    private fun mapBookDataList(books: List<BookData>) = books.map(bookDomainMapper::map)

    override fun fetchBooksFromCloud(schoolId: String, userId: String): Flow<List<BookDomain>> =
        cloudDataSource.fetchBooks(schoolId = schoolId, userId = userId)
            .map { books -> books.map(bookDomainMapper::map) }
            .flowOn(dispatchersProvider.default())

    override suspend fun addNewBook(book: AddNewBookDomain) = renderResultToUnit(
        result = cloudDataSource.addNewBook(book = addBookDomainMapper.map(book)),
        onSuccess = { data ->
            cacheDataSource.addNewBook(book = book.mapToBook(data.objectId, data.createdAt))
        }
    )

    override suspend fun fetchBookPdfFileForReading(url: String): RequestState<InputStream> {
        return try {
            val urlConnection = URL(url).openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == 200) {
                RequestState.Success(BufferedInputStream(urlConnection.inputStream))
            } else RequestState.Error(IllegalStateException())
        } catch (error: Throwable) {
            RequestState.Error(error)
        }
    }

    override suspend fun deleteBook(id: String): RequestState<Unit> {
        return when (val result = cloudDataSource.deleteBook(id = id)) {
            is CloudDataRequestState.Success -> handleSuccessDeleteBook(id)
            is CloudDataRequestState.Error -> RequestState.Error(result.error)
        }
    }

    private suspend fun handleSuccessDeleteBook(id: String) = renderResultToUnit(
        result = cloudDataSource.fetchSavedBookById(bookId = id),
        onSuccess = {
            cacheDataSource.deleteBook(id = id)
            it.books.forEach { bookThatReadData ->
                cloudDataSource.deleteMyBook(id = bookThatReadData.objectId)
                cacheDataSource.deleteMyBookIsCache(id = id)
            }
        }
    )

    override suspend fun fetchChapterQuestions(id: String, chapter: String) =
        when (val result = cloudDataSource.fetchChapterQuestions(id = id, chapter = chapter)) {
            is CloudDataRequestState.Success -> RequestState.Success(
                result.data.questions.map(questionCloudMapper::map).map(questionsMapper::map)
            )
            is CloudDataRequestState.Error -> RequestState.Error(result.error)
        }

    override suspend fun addBookQuestion(question: AddBookQuestionDomain) = renderResult(
        result = cloudDataSource.addBookQuestion(question = questionsDomainMapper.map(question))
    )

    override suspend fun deleteBookQuestion(id: String) = renderResult(
        result = cloudDataSource.deleteBookQuestion(id = id)
    )

    override suspend fun updateBookQuestion(
        id: String,
        question: AddBookQuestionDomain,
    ) = renderResultToUnit(
        result = cloudDataSource.updateQuestion(
            id = id,
            question = questionsDomainMapper.map(question)
        )
    )

    override suspend fun updateBook(id: String, book: UpdateBookDomain) = renderResultToUnit(
        result = cloudDataSource.updateBook(id = id, book = updateBookDomainMapper.map(book)),
        onSuccess = {
            cacheDataSource.updateTitle(id = id, title = book.title)
            cacheDataSource.updateAuthor(id = id, author = book.author)
            cacheDataSource.updatePublicYear(id = id, publicYear = book.publicYear)
            cacheDataSource.updatePoster(id = id, poster = book.poster)
        }
    )

    override suspend fun clearBooksCache() {
        cacheDataSource.clearTable()
    }
}

