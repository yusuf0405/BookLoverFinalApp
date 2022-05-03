package com.example.data.data.cache.source

import com.example.data.data.cache.db.BooksThatReadDao
import com.example.data.data.cache.models.BookThatReadDb
import com.example.data.data.models.BookThatReadData
import com.example.domain.domain.Mapper

interface BooksThatReadDataSource {

    suspend fun fetchBooksThatRead(): List<BookThatReadDb>

    suspend fun saveBooks(books: List<BookThatReadData>)

    suspend fun deleteBook(id: String)

    suspend fun getMyBook(id: String): BookThatReadDb

    suspend fun addBook(book: BookThatReadDb)

    suspend fun updateProgress(id: String, progress: Int)

    suspend fun updateChapters(id: String, chapters: Int)

    suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>)

    suspend fun clearTable()

    class Base(
        private val dao: BooksThatReadDao,
        private val mapper: Mapper<BookThatReadData, BookThatReadDb>,
    ) :
        BooksThatReadDataSource {

        override suspend fun fetchBooksThatRead(): List<BookThatReadDb> = dao.getAllBooks()

        override suspend fun saveBooks(books: List<BookThatReadData>) {
            books.map { book -> dao.addNewBook(book = mapper.map(book)) }
        }

        override suspend fun deleteBook(id: String) = dao.deleteById(id = id)

        override suspend fun getMyBook(id: String): BookThatReadDb = dao.getMyBook(bookId = id)


        override suspend fun addBook(book: BookThatReadDb) = dao.addNewBook(book = book)

        override suspend fun updateProgress(id: String, progress: Int) =
            dao.updateProgress(progress, id = id)

        override suspend fun updateChapters(id: String, chapters: Int) =
            dao.updateChapters(chapters = chapters, id = id)

        override suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>) =
            dao.updateIsReadingPages(isReadingPages = isReadingPages, id = id)

        override suspend fun clearTable() = dao.clearTable()

    }
}