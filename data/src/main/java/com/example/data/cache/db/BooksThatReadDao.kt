package com.example.data.cache.db

import androidx.room.*
import com.example.data.cache.models.BookThatReadDb


@Dao
interface BooksThatReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(book: BookThatReadDb)

    @Update
    suspend fun updateBook(book: BookThatReadDb)

    @Query("UPDATE book_that_read_database SET progress =:progress WHERE objectId = :id")
    suspend fun updateProgress(progress: Int, id: String)

    @Query("UPDATE book_that_read_database SET chaptersRead =:chapters WHERE objectId = :id")
    suspend fun updateChapters(chapters: Int, id: String)

    @Query("UPDATE book_that_read_database SET isReadingPages =:isReadingPages WHERE objectId = :id")
    suspend fun updateIsReadingPages(isReadingPages: List<Boolean>, id: String)

    @Delete
    suspend fun deleteBook(book: BookThatReadDb)

    @Query("DELETE FROM book_that_read_database WHERE objectId = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM book_that_read_database")
    fun clearTable()

    @Query("select * from book_that_read_database")
    suspend fun getAllBooks(): MutableList<BookThatReadDb>

    @Query("select * from book_that_read_database where bookId == :bookId")
    suspend fun getMyBook(bookId: String): BookThatReadDb
}
