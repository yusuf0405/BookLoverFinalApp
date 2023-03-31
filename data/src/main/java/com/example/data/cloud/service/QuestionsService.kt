package com.example.data.cloud.service

import com.example.data.cloud.models.QuestionPosterCloudResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuestionsService {

    @GET("classes/QiestionPoster")
    suspend fun fetchAllQuestionPosters(): Response<QuestionPosterCloudResponse>
}