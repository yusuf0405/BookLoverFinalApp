package com.example.domain.repository

import com.example.domain.models.AudioBookDomain
import kotlinx.coroutines.flow.Flow

interface AudioBooksRepository {

    fun fetchAllAudioBooks(schoolId: String): Flow<List<AudioBookDomain>>

    fun fetchAllAudioBooksFromCache(): Flow<List<AudioBookDomain>>

    fun fetchAudioBookFromCacheObservable(audioBookId: String): Flow<AudioBookDomain>

    suspend fun updateAudioBookCurrentStartPosition(audioBookId: String, currentPosition: Int)

    suspend fun updateAudioBookIsPlayingState(audioBookId: String, isPlaying: Boolean)

    suspend fun clearTable()
}