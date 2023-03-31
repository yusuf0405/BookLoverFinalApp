package com.example.data.cloud.source.stories

import com.example.data.base.ResponseHandler
import com.example.data.cloud.models.AddStoriesCloud
import com.example.data.cloud.models.StoriesCloud
import com.example.data.cloud.models.StoriesResponse
import com.example.data.cloud.service.StoriesService
import com.example.data.models.StoriesData
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoriesCloudDataSourceImpl @Inject constructor(
    private var service: StoriesService,
    private var dispatchersProvider: DispatchersProvider,
    private val responseHandler: ResponseHandler,
    private var storiesCloudToDataMapper: Mapper<StoriesCloud, StoriesData>
) : StoriesCloudDataSource {

    override fun fetchAllStoriesFromCloud(schoolId: String): Flow<List<StoriesData>> = flow {
        emit(service.fetchSchoolStories(id = "{\"schoolId\":\"${schoolId}\"}"))
    }.flowOn(dispatchersProvider.io())
        .map { it.body() ?: StoriesResponse(emptyList()) }
        .map { it.stories }
        .map { books -> books.map(storiesCloudToDataMapper::map) }
        .flowOn(dispatchersProvider.default())

    override suspend fun addNewStories(addNewStories: AddStoriesCloud) =
        responseHandler.safeApiCall { service.addNewStories(addNewStories) }

}