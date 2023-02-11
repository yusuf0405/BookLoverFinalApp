package com.example.data.cloud.source

import com.example.data.cloud.models.GenreCloud
import com.example.data.cloud.models.GenreResponse
import com.example.data.cloud.service.GenreService
import com.example.data.models.GenreData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenresCloudDataSourceImpl @Inject constructor(
    private val service: GenreService,
    private val dispatchersProvider: DispatchersProvider,
    private val genreCloudToDataMapper: Mapper<GenreCloud, GenreData>
) : GenresCloudDataSource {

    override fun fetchAllGenresFromCloud(): Flow<List<GenreData>> = flow {
        emit(service.fetchAllGenres())
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: GenreResponse(emptyList()) }
        .map { it.genres }
        .map { genres -> genres.map(genreCloudToDataMapper::map) }
        .flowOn(dispatchersProvider.default())
}