package com.example.data.cache.source.audio_books

import com.example.data.models.AudioBookData
import kotlinx.coroutines.flow.Flow

interface AudioBooksCacheDataSource {

    fun fetchAllAudioBooksFromCacheObservable(): Flow<List<AudioBookData>>

    suspend fun fetchAllAudioBooksFromCacheSingle(): List<AudioBookData>

    suspend fun saveNewAudioBooksToCache(audioBook: AudioBookData)

    fun fetchAudioBookFromIdObservable(audioBookId: String): Flow<AudioBookData>

    suspend fun updateAudioBookCurrentStartPosition(audioBookId: String, currentPosition: Int)

    suspend fun updateAudioBookIsPlayingState(audioBookId: String, isPlaying: Boolean)

    suspend fun clearTable()
}