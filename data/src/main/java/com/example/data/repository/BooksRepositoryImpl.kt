package com.example.data.repository

import com.example.data.ResourceProvider
import com.example.data.cache.models.BookCache
import com.example.data.cache.source.BooksCacheDataSource
import com.example.data.cloud.source.BooksCloudDataSource
import com.example.data.models.*
import com.example.data.toBook
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.models.*
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class BooksRepositoryImpl(
    private val resourceProvider: ResourceProvider,
    private val cloudDataSource: BooksCloudDataSource,
    private val cacheDataSource: BooksCacheDataSource,
    private val bookCashMapper: Mapper<BookCache, BookData>,
    private val bookDomainMapper: Mapper<BookData, BookDomain>,
    private val updateBookDomainMapper: Mapper<UpdateBookDomain, UpdateBookData>,
    private val addBookDomainMapper: Mapper<AddNewBookDomain, AddNewBookData>,
    private val questionsMapper: Mapper<BookQuestionData, BookQuestionDomain>,
    private val questionsDomainMapper: Mapper<AddBookQuestionDomain, AddBookQuestionData>,
) : BooksRepository {

    override fun fetchBooks(schoolId: String): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        val booksCacheList = cacheDataSource.fetchBooks()
        if (booksCacheList.isEmpty()) {
            val result = cloudDataSource.fetchBooks(schoolId = schoolId)
            if (result.status == Status.SUCCESS) {
                val booksData = result.data!!
                if (booksData.isEmpty()) emit(Resource.empty())
                else {
                    cacheDataSource.saveBooks(books = booksData)
                    val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
                    emit(Resource.success(data = booksDomain))
                }
            } else emit(Resource.error(message = result.message))
        } else {
            val booksData = booksCacheList.map { bookDb -> bookCashMapper.map(bookDb) }
            val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
            if (booksDomain.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = booksDomain))
        }
    }

    override fun onRefresh(schoolId: String): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.fetchBooks(schoolId = schoolId)
        if (result.status == Status.SUCCESS) {
            val booksData = result.data!!
            if (booksData.isEmpty()) emit(Resource.empty())
            else {
                cacheDataSource.saveBooks(books = booksData)
                val booksDomain = booksData.map { bookData -> bookDomainMapper.map(bookData) }
                emit(Resource.success(data = booksDomain))
            }
        } else emit(Resource.error(message = result.message))
    }

    override fun fetchSimilarBooks(
        genres: List<String>,
        bookId: String,
    ): Flow<Resource<List<BookDomain>>> = flow {
        emit(Resource.loading())
        delay(3000)
        val booksCacheList = cacheDataSource.fetchBooks()
        val similarBooks = mutableListOf<BookDomain>()
        genres.forEach { genre ->
            booksCacheList.forEach { book ->
                book.genres.forEach { newGenres ->
                    if (genre == newGenres && book.id != bookId) {
                        val bookData = bookCashMapper.map(book)
                        similarBooks.add(bookDomainMapper.map(bookData))
                    }
                }
            }
        }
        val finalList = similarBooks.distinctBy { it.id }
        if (finalList.isEmpty()) emit(Resource.empty())
        else emit(Resource.success(data = finalList))
    }

    override fun addNewBook(book: AddNewBookDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addNewBook(book = addBookDomainMapper.map(book))
        if (result.status == Status.SUCCESS) {
            cacheDataSource.addNewBook(book = book.toBook(id = result.data!!.objectId,
                createdAt = result.data!!.createdAt))
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message!!))
    }

    override fun getBookForReading(url: String): Flow<Resource<InputStream>> = flow {
        try {
            emit(Resource.loading())
            val urlConnection = URL(url).openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == 200) {
                emit(Resource.success(BufferedInputStream(urlConnection.inputStream)))
            } else emit(Resource.error(message = urlConnection.responseMessage))
        } catch (e: Exception) {
            emit(Resource.error(message = e.message))
        }

    }

    override fun deleteBook(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteBook(id = id)
        if (result.status == Status.SUCCESS) {
            val booksResult = cloudDataSource.fetchMyBooks(bookId = id)
            if (booksResult.status == Status.SUCCESS) {
                val booksThatRead = booksResult.data!!
                if (booksThatRead.isNotEmpty()) {
                    booksThatRead.forEach { bookThatReadData ->
                        cloudDataSource.deleteMyBook(id = bookThatReadData.objectId)
                        cacheDataSource.deleteMyBookIsCache(id = id)
                    }
                }
                cacheDataSource.deleteBook(id = id)
                emit(Resource.success(data = Unit))
            } else emit(Resource.error(message = booksResult.message))
        } else emit(Resource.error(message = result.message))
    }

    override fun fetchChapterQuestions(
        id: String,
        chapter: String,
    ): Flow<Resource<List<BookQuestionDomain>>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.fetchChapterQuestions(id = id, chapter = chapter)
        if (result.status == Status.SUCCESS) {
            if (result.data!!.isEmpty()) emit(Resource.empty())
            else emit(Resource.success(data = result.data!!.map { questionData ->
                questionsMapper.map(questionData)
            }))
        } else emit(Resource.error(message = result.message!!))
    }

    override fun fetchSearchBook(
        searchText: String,
        schoolId: String,
    ): Flow<Resource<List<BookDomain>>> = flow {
        try {
            val allBooks = cacheDataSource.fetchBooks()
            val searchBooks = mutableListOf<BookDomain>()
            allBooks.forEach { book ->
                val title = book.title.lowercase()
                if (title.contains(searchText)) searchBooks.add(bookDomainMapper.map(bookCashMapper.map(
                    book)))
            }
            if (searchBooks.isNotEmpty()) emit(Resource.success(searchBooks))
            else emit(Resource.empty())
        } catch (e: Exception) {
            emit(Resource.error(resourceProvider.errorType(e)))
        }

    }

    override fun addBookQuestion(question: AddBookQuestionDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.addBookQuestion(question = questionsDomainMapper.map(question))
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))
    }

    override fun deleteBookQuestion(id: String): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.deleteBookQuestion(id = id)
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))
    }

    override fun updateBookQuestion(
        id: String,
        question: AddBookQuestionDomain,
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result =
            cloudDataSource.updateQuestion(id = id, question = questionsDomainMapper.map(question))
        if (result.status == Status.SUCCESS) emit(Resource.success(data = Unit))
        else emit(Resource.error(message = result.message!!))

    }

    override fun updateBook(id: String, book: UpdateBookDomain): Flow<Resource<Unit>> = flow {
        emit(Resource.loading())
        val result = cloudDataSource.updateBook(id = id, book = updateBookDomainMapper.map(book))
        if (result.status == Status.SUCCESS) {
            cacheDataSource.updateTitle(id = id, title = book.title)
            cacheDataSource.updateAuthor(id = id, author = book.author)
            cacheDataSource.updatePublicYear(id = id, publicYear = book.publicYear)
            cacheDataSource.updatePoster(id = id, poster = book.poster)
            emit(Resource.success(data = Unit))
        } else emit(Resource.error(message = result.message))
    }

    override suspend fun clearBooksCache() = cacheDataSource.clearTable()

}

