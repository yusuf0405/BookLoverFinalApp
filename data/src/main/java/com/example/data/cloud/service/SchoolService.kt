package com.example.data.cloud.service

import com.example.data.cloud.models.ClassResponse
import com.example.data.models.school.SchoolResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface SchoolService {

    @GET("classes/Classes")
    suspend fun getAllClasses(): Response<ClassResponse>

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassResponse>

    @GET("classes/School")
    suspend fun getAllSchools(): Response<SchoolResponse>

}