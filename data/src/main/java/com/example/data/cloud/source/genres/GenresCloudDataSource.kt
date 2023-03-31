package com.example.data.cloud.source.genres

import com.example.data.models.GenreData
import kotlinx.coroutines.flow.Flow

interface GenresCloudDataSource {

    fun fetchAllGenresFromCloud(): Flow<List<GenreData>>

}