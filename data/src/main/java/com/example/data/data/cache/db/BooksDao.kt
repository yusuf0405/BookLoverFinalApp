package com.example.data.data.cache.db

import androidx.room.*
import com.example.data.data.cache.models.BookDb


@Dao
interface BooksDao {

    @Insert
    suspend fun addNewBook(book: BookDb)

    @Update
    suspend fun updateBook(book: BookDb)

    @Query("DELETE FROM book_database")
    fun clearTable()

    @Delete
    suspend fun deleteBook(book: BookDb)

    @Query("select * from book_database")
    suspend fun getAllBooks(): MutableList<BookDb>

    @Query("select * from book_database where id == :bookId")
    suspend fun getBook(bookId: Int): BookDb
}
