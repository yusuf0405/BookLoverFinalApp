package com.example.domain.repository

import com.example.domain.RequestState
import com.example.domain.models.AddStoriesDomain
import com.example.domain.models.StoriesDomain
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {

    fun fetchAllStories(schoolId: String): Flow<List<StoriesDomain>>

    fun fetchAllStoriesFromCache(): Flow<List<StoriesDomain>>

    suspend fun fetchStoriesFromCache(storiesId: String): StoriesDomain

    suspend fun addNewStories(newStories: AddStoriesDomain): RequestState<Unit>

    suspend fun clearTable()
}