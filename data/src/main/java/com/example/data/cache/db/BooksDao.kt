package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.*
import kotlinx.coroutines.flow.Flow


@Dao
interface BooksDao {

    @Query("select * from books")
    fun fetchAllBooksObservable(): Flow<MutableList<BookCache>>

    @Query("select * from books")
    suspend fun fetchAllBooksSingle(): MutableList<BookCache>

    @Query("select * from books where id == :bookId")
    suspend fun fetchBookFromId(bookId: String): BookCache

    @Query("select * from books where id == :bookId")
    fun fetchBookObservable(bookId: String): Flow<BookCache?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(book: BookCache)

    @Query("UPDATE books SET title  =:title WHERE id = :id")
    suspend fun updateBookTitle(title: String, id: String)

    @Query("UPDATE books SET author =:author WHERE id = :id")
    suspend fun updateBookAuthor(author: String, id: String)

    @Query("UPDATE books SET public_year =:publicYear WHERE id = :id")
    suspend fun updateBookPublicYear(publicYear: String, id: String)

    @Query("UPDATE books SET poster =:poster WHERE id = :id")
    suspend fun updateBookPoster(poster: BookPosterCache, id: String)

    @Query("UPDATE books SET saved_status =:savedStatus WHERE id = :id")
    suspend fun updateCacheBookSavedStatus(savedStatus: SavedStatusCache, id: String)

    @Query("DELETE FROM books WHERE id = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM books")
    fun clearTable()
}
