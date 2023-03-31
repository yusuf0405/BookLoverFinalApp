package com.example.data.cloud.source.questions

import com.example.data.ResourceProvider
import com.example.data.base.BaseApiResponse
import com.example.data.cloud.service.QuestionsService
import com.example.domain.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionsCloudDataSourceImpl @Inject constructor(
    private val service: QuestionsService,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider
) : QuestionsCloudDataSource, BaseApiResponse(resourceProvider) {

    override suspend fun fetchAllQuestionsPosters(): List<String> {
        val response = safeApiCall { service.fetchAllQuestionPosters() }
        return withContext(dispatchersProvider.default()) {
            return@withContext response.data?.posters?.map { it.poster?.url ?: "" } ?: emptyList()
        }
    }
}