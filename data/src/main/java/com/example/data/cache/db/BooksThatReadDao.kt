package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.BookThatReadCache
import kotlinx.coroutines.flow.Flow


@Dao
interface BooksThatReadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(book: BookThatReadCache)

    @Query("select * from books_that_read")
    fun fetchAllBooksObservable(): Flow<MutableList<BookThatReadCache>>

    @Query("select * from books_that_read")
    suspend fun fetchAllBooksSingle(): MutableList<BookThatReadCache>

    @Query("select * from books_that_read where book_id == :bookId")
    suspend fun fetchBooksByBookId(bookId: String): BookThatReadCache?

    @Query("select * from books_that_read where book_id == :bookId")
    suspend fun getMyBook(bookId: String): BookThatReadCache?

    @Query("UPDATE books_that_read SET progress =:progress WHERE objectId = :id")
    suspend fun updateProgress(progress: Int, id: String)

    @Query("UPDATE books_that_read SET chapters_read =:chapters WHERE objectId = :id")
    suspend fun updateChapters(chapters: Int, id: String)

    @Query("UPDATE books_that_read SET is_reading_pages =:isReadingPages WHERE objectId = :id")
    suspend fun updateIsReadingPages(isReadingPages: List<Boolean>, id: String)

    @Query("DELETE FROM books_that_read WHERE objectId = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM books_that_read")
    fun clearTable()
}
