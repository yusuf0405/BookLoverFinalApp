package com.example.data.cache.db

import androidx.room.*
import com.example.data.cache.models.BookDb
import com.example.data.cache.models.BookPosterDb


@Dao
interface BooksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(book: BookDb)

    @Update
    suspend fun updateBook(book: BookDb)

    @Query("UPDATE book_database SET title =:title WHERE id = :id")
    suspend fun updateBookTitle(title: String, id: String)

    @Query("UPDATE book_database SET author =:author WHERE id = :id")
    suspend fun updateBookAuthor(author: String, id: String)

    @Query("UPDATE book_database SET publicYear =:publicYear WHERE id = :id")
    suspend fun updateBookPublicYear(publicYear: String, id: String)

    @Query("UPDATE book_database SET poster =:poster WHERE id = :id")
    suspend fun updateBookPoster(poster: BookPosterDb, id: String)


    @Query("DELETE FROM book_database")
    fun clearTable()

    @Delete
    suspend fun deleteBook(book: BookDb)

    @Query("DELETE FROM book_database WHERE id = :id")
    fun deleteById(id: String)

    @Query("select * from book_database")
    suspend fun getAllBooks(): MutableList<BookDb>

    @Query("select * from book_database where id == :bookId")
    suspend fun getBook(bookId: String): BookDb
}
