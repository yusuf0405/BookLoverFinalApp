package com.example.data.cache.source.stories

import com.example.data.models.StoriesData
import kotlinx.coroutines.flow.Flow

interface StoriesCacheDataSource {

    fun fetchAllStoriesFromCacheObservable(): Flow<List<StoriesData>>

    suspend fun fetchAllStoriesFromCacheSingle(): List<StoriesData>

    suspend fun saveNewStoriesToCache(stories: StoriesData)

    suspend fun fetchStoriesFromId(storiesId: String): StoriesData

    suspend fun clearTable()
}