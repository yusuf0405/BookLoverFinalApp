package com.example.data.cache.source.books_that_read

import com.example.data.cache.db.BooksThatReadDao
import com.example.data.cache.models.BookThatReadCache
import com.example.data.models.BookThatReadData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BooksThatReadCacheDataSourceImpl @Inject constructor(
    private val dao: BooksThatReadDao,
    private val dispatchersProvider: DispatchersProvider,
    private val bookThatReadDataToCacheMapper: Mapper<BookThatReadData, BookThatReadCache>,
) : BooksThatReadCacheDataSource {

    override fun fetchBooksThatReadObservable(): Flow<List<BookThatReadCache>> =
        dao.fetchAllBooksObservable().flowOn(dispatchersProvider.io())

    override suspend fun fetchBooksThatReadSingle(): List<BookThatReadCache> =
        dao.fetchAllBooksSingle()

    override suspend fun fetchBooksByBookId(bookId: String): BookThatReadCache? =
        dao.fetchBooksByBookId(bookId = bookId)

    override suspend fun saveBooks(books: List<BookThatReadData>) {
        books.map { book -> dao.addNewBook(book = bookThatReadDataToCacheMapper.map(book)) }
    }

    override suspend fun deleteBook(id: String) = dao.deleteById(id = id)

    override suspend fun getMyBook(id: String): BookThatReadCache? = dao.getMyBook(bookId = id)

    override suspend fun addBook(book: BookThatReadCache) = dao.addNewBook(book = book)

    override suspend fun updateProgress(id: String, progress: Int) =
        dao.updateProgress(progress, id = id)

    override suspend fun updateChapters(id: String, chapters: Int) =
        dao.updateChapters(chapters = chapters, id = id)

    override suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>) =
        dao.updateIsReadingPages(isReadingPages = isReadingPages, id = id)

    override suspend fun clearTable() = dao.clearTable()
}