package com.example.data.data.cache.source

import com.example.data.data.cache.models.StudentBookDb
import com.example.domain.domain.Mapper
import com.example.data.data.cache.db.StudentBooksDao
import com.example.data.data.models.StudentBookData

interface StudentBookCacheDataSource {

    suspend fun fetchStudentBooks(): List<StudentBookDb>


    suspend fun saveBooks(books: List<StudentBookData>)

    suspend fun deleteBook(id: String)

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

    }
}