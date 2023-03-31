package com.joseph.stories.domain.repositories

import com.joseph.stories.presentation.models.StoriesModel
import kotlinx.coroutines.flow.Flow

interface StoriesFeatureRepository {

    fun fetchAllStoriesFromCloud(): Flow<List<StoriesModel>>

    fun fetchAllStoriesFromCache(): Flow<List<StoriesModel>>
}