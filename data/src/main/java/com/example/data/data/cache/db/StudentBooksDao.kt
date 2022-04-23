package com.example.data.data.cache.db

import androidx.room.*
import com.example.data.data.cache.models.StudentBookDb


@Dao
interface StudentBooksDao {

    @Insert
    suspend fun addNewBook(book: StudentBookDb)

    @Update
    suspend fun updateBook(book: StudentBookDb)

    @Delete
    suspend fun deleteBook(book: StudentBookDb)

    @Query("DELETE FROM student_book_database WHERE objectId = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM student_book_database")
    fun clearTable()

    @Query("select * from student_book_database")
    suspend fun getAllBooks(): MutableList<StudentBookDb>

    @Query("select * from student_book_database where objectId == :bookId")
    suspend fun getBook(bookId: String): StudentBookDb
}
