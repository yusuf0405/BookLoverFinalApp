package com.example.data.cloud.service

import com.example.data.cloud.models.TaskResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TaskService {

    @GET("classes/Task")
    suspend fun fetchAllAudioBooks(
        @Query("where") id: String,
    ): Response<TaskResponse>

}