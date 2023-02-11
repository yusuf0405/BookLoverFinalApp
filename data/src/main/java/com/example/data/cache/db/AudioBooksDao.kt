package com.example.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.cache.models.AudioBookCache
import com.example.data.cache.models.BookThatReadCache
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioBooksDao {

    @Query("select * from AUDIO_BOOKS_TABLE")
    fun fetchAllAudioBooksObservable(): Flow<MutableList<AudioBookCache>>

    @Query("select * from AUDIO_BOOKS_TABLE")
    suspend fun fetchAllAudioBooksSingle(): MutableList<AudioBookCache>

    @Query("select * from AUDIO_BOOKS_TABLE where id == :audioBookId")
    suspend fun fetchAudioBookFromId(audioBookId: String): AudioBookCache

    @Query("UPDATE AUDIO_BOOKS_TABLE SET current_start_position  =:currentPosition WHERE id = :audioBookId")
    suspend fun updateAudioBookCurrentStartPosition(
        audioBookId: String,
        currentPosition: Int
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewBook(audioBook: AudioBookCache)

    @Query("DELETE FROM AUDIO_BOOKS_TABLE")
    fun clearTable()

}