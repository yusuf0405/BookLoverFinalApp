package com.example.data.cloud.source.stories

import com.example.data.cloud.CloudDataRequestState
import com.example.data.cloud.models.AddStoriesCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.models.StoriesData
import kotlinx.coroutines.flow.Flow

interface StoriesCloudDataSource {

    fun fetchAllStoriesFromCloud(schoolId: String): Flow<List<StoriesData>>

   suspend fun addNewStories(addNewStories: AddStoriesCloud): CloudDataRequestState<PostRequestAnswerCloud>
}