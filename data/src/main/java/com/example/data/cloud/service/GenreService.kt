package com.example.data.cloud.service

import com.example.data.cloud.models.GenreResponse
import retrofit2.Response
import retrofit2.http.GET

interface GenreService {

    @GET("classes/Genres")
    suspend fun fetchAllGenres(): Response<GenreResponse>

}