package com.example.data.cloud.service

import com.example.data.cloud.models.AddStoriesCloud
import com.example.data.cloud.models.PostRequestAnswerCloud
import com.example.data.cloud.models.StoriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StoriesService {

    @GET("classes/UserStories")
    suspend fun fetchSchoolStories(
        @Query("where") id: String,
    ): Response<StoriesResponse>

    @POST("classes/UserStories")
    suspend fun addNewStories(
        @Body stories: AddStoriesCloud,
    ): Response<PostRequestAnswerCloud>
}