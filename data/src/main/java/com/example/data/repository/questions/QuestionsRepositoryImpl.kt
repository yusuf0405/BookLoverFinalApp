package com.example.data.repository.questions

import com.example.data.cloud.source.questions.QuestionsCloudDataSource
import com.example.domain.repository.QuestionsRepository
import javax.inject.Inject

class QuestionsRepositoryImpl @Inject constructor(
    private val cloudDataSource: QuestionsCloudDataSource
) : QuestionsRepository {

    override suspend fun fetchAllQuestionsPosters(): List<String> {
        return cloudDataSource.fetchAllQuestionsPosters()
    }
}