package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.BookThatReadCache
import com.example.data.cache.models.GenreCache
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("select * from GENRES_TABLE")
    fun fetchAllGenresObservable(): Flow<MutableList<GenreCache>>

    @Query("select * from GENRES_TABLE")
    suspend fun fetchAllGenresSingle(): MutableList<GenreCache>

    @Query("select * from GENRES_TABLE where id == :genreId")
    suspend fun fetchGenreFromId(genreId: String): GenreCache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(genre: GenreCache)

    @Query("DELETE FROM GENRES_TABLE")
    fun clearTable()

}