package com.example.data.repository

import com.example.data.cache.source.genres.GenresCacheDataSource
import com.example.data.cloud.source.GenresCloudDataSource
import com.example.data.models.GenreData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.GenreDomain
import com.example.domain.repository.GenresRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GenresRepositoryImpl @Inject constructor(
    private val cloudDataSource: GenresCloudDataSource,
    private val cacheDataSource: GenresCacheDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val genreDataToDomainMapper: Mapper<GenreData, GenreDomain>
) : GenresRepository {


    override fun fetchAllGenres(): Flow<List<GenreDomain>> = flow {
        emit(cacheDataSource.fetchAllGenresFromCacheSingle())
    }.flatMapLatest { handleFetchGenresInCache(it) }
        .map { genres -> genres.map(genreDataToDomainMapper::map) }
        .flowOn(dispatchersProvider.default())

    private fun handleFetchGenresInCache(
        cachedBooks: List<GenreData>,
    ) = if (cachedBooks.isEmpty()) cloudDataSource.fetchAllGenresFromCloud()
        .onEach { books -> books.forEach { cacheDataSource.addNewGenreToCache(it) } }
    else cacheDataSource.fetchAllGenresFromCacheObservable()

    override suspend fun fetchGenreFromCache(genreId: String): GenreDomain {
        return genreDataToDomainMapper.map(cacheDataSource.fetchGenreFromId(genreId = genreId))
    }
}