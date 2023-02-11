package com.example.data.cache.source.genres

import com.example.data.cache.db.GenreDao
import com.example.data.cache.models.GenreCache
import com.example.data.models.GenreData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenresCacheDataSourceImpl @Inject constructor(
    private val dao: GenreDao,
    private val dispatchersProvider: DispatchersProvider,
    private val genreCacheToDataMapper: Mapper<GenreCache, GenreData>,
    private val genreDataToCacheMapper: Mapper<GenreData, GenreCache>
) : GenresCacheDataSource {

    override fun fetchAllGenresFromCacheObservable(): Flow<List<GenreData>> =
        dao.fetchAllGenresObservable()
            .flowOn(dispatchersProvider.io())
            .map { genres -> genres.map(genreCacheToDataMapper::map) }
            .flowOn(dispatchersProvider.default())


    override suspend fun fetchAllGenresFromCacheSingle(): List<GenreData> {
        val cachedList = dao.fetchAllGenresSingle()
        return cachedList.map(genreCacheToDataMapper::map)
    }

    override suspend fun addNewGenreToCache(genre: GenreData) {
        dao.addNewBook(genreDataToCacheMapper.map(genre))
    }

    override suspend fun fetchGenreFromId(genreId: String): GenreData =
        genreCacheToDataMapper.map(dao.fetchGenreFromId(genreId = genreId))


    override suspend fun clearTable() {
        dao.clearTable()
    }
}