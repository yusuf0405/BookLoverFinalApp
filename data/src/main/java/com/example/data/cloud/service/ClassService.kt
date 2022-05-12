package com.example.data.cloud.service

import com.example.data.cloud.models.ClassResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassService {

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassResponse>

    @DELETE("classes/Classes/{id}")
    suspend fun deleteClass(
        @Path("id") id: String,
    ): Response<Unit>
}