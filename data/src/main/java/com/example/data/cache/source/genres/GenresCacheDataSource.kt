package com.example.data.cache.source.genres

import com.example.data.models.GenreData
import kotlinx.coroutines.flow.Flow

interface GenresCacheDataSource {

    fun fetchAllGenresFromCacheObservable(): Flow<List<GenreData>>

    suspend fun fetchAllGenresFromCacheSingle(): List<GenreData>

    suspend fun addNewGenreToCache(genre: GenreData)

    suspend fun fetchGenreFromId(genreId: String): GenreData

    suspend fun clearTable()
}