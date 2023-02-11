package com.example.domain.repository

import com.example.domain.models.AudioBookDomain
import kotlinx.coroutines.flow.Flow

interface AudioBooksRepository {

    fun fetchAllAudioBooks(schoolId: String): Flow<List<AudioBookDomain>>

    fun fetchAllAudioBooksFromCache(): Flow<List<AudioBookDomain>>

    suspend fun fetchAudioBookFromCache(audioBookId: String): AudioBookDomain

    suspend fun updateAudioBookCurrentStartPosition(audioBookId: String, currentPosition: Int)

    suspend fun clearTable()
}