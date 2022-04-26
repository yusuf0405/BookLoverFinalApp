package com.example.data.data.cache.source

import com.example.data.data.cache.db.StudentBooksDao
import com.example.data.data.cache.models.StudentBookDb
import com.example.data.data.models.StudentBookData
import com.example.domain.domain.Mapper

interface StudentBookCacheDataSource {

    suspend fun fetchStudentBooks(): List<StudentBookDb>


    suspend fun saveBooks(books: List<StudentBookData>)


    suspend fun deleteBook(id: String)


    suspend fun getMyBook(id: String): StudentBookDb


    suspend fun addBook(book: StudentBookDb)

    suspend fun updateProgress(id: String, progress: Int)

    suspend fun updateChapters(id: String, chapters: Int)

    suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>)

    class Base(
        private val dao: StudentBooksDao,
        private val mapper: Mapper<StudentBookData, StudentBookDb>,
    ) :
        StudentBookCacheDataSource {

        override suspend fun fetchStudentBooks(): List<StudentBookDb> = dao.getAllBooks()

        override suspend fun saveBooks(books: List<StudentBookData>) {
            books.map { book -> dao.addNewBook(book = mapper.map(book)) }
        }

        override suspend fun deleteBook(id: String) = dao.deleteById(id = id)

        override suspend fun getMyBook(id: String): StudentBookDb = dao.getMyBook(bookId = id)


        override suspend fun addBook(book: StudentBookDb) = dao.addNewBook(book = book)

        override suspend fun updateProgress(id: String, progress: Int) =
            dao.updateProgress(progress, id = id)

        override suspend fun updateChapters(id: String, chapters: Int) =
            dao.updateChapters(chapters = chapters, id = id)

        override suspend fun updateIsReadIsPages(id: String, isReadingPages: List<Boolean>) =
            dao.updateIsReadingPages(isReadingPages = isReadingPages, id = id)

    }
}