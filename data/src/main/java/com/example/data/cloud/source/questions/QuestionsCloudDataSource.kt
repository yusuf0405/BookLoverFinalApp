package com.example.data.cloud.source.questions

interface QuestionsCloudDataSource {

   suspend fun fetchAllQuestionsPosters(): List<String>
}