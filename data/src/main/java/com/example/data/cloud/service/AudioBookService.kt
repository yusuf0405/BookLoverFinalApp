package com.example.data.cloud.service

import com.example.data.cloud.models.AudioBookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioBookService {

    @GET("classes/AudioBooks")
    suspend fun fetchAllAudioBooks(
        @Query("where") id: String,
    ): Response<AudioBookResponse>

}