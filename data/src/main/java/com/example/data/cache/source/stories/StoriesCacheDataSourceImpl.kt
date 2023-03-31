package com.example.data.cache.source.stories

import com.example.data.cache.db.StoriesDao
import com.example.data.cache.models.StoriesCache
import com.example.data.models.StoriesData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoriesCacheDataSourceImpl @Inject constructor(
    private val dao: StoriesDao,
    private val dispatchersProvider: DispatchersProvider,
    private val storiesCacheToDataMapper: Mapper<StoriesCache, StoriesData>,
    private val storiesDataToCacheMapper: Mapper<StoriesData, StoriesCache>
) : StoriesCacheDataSource {

    override fun fetchAllStoriesFromCacheObservable(): Flow<List<StoriesData>> =
        dao.fetchStoriesObservable()
            .flowOn(dispatchersProvider.io())
            .map { books -> books.map(storiesCacheToDataMapper::map) }
            .flowOn(dispatchersProvider.default())

    override suspend fun fetchAllStoriesFromCacheSingle(): List<StoriesData> =
        dao.fetchStoriesSingle().map(storiesCacheToDataMapper::map)

    override suspend fun saveNewStoriesToCache(stories: StoriesData) {
        dao.addNewStories(storiesDataToCacheMapper.map(stories))
    }

    override suspend fun fetchStoriesFromId(storiesId: String): StoriesData {
        val stories = dao.fetchStoriesFromId(storiesId)
        return storiesCacheToDataMapper.map(stories)
    }

    override suspend fun clearTable() {
        dao.clearTable()
    }
}