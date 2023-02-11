package com.example.domain.repository

import com.example.domain.models.GenreDomain
import kotlinx.coroutines.flow.Flow

interface GenresRepository {

    fun fetchAllGenres(): Flow<List<GenreDomain>>

    suspend fun fetchGenreFromCache(genreId: String): GenreDomain

}