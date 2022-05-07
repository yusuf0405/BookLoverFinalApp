package com.example.data.api

import com.example.data.models.classes.ClassesResponse
import com.example.data.models.school.SchoolResponse
import retrofit2.Response
import retrofit2.http.*


interface KnigolyubApi {

    @GET("classes/Classes")
    suspend fun getAllClasses(): Response<ClassesResponse>

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassesResponse>

    @GET("classes/School")
    suspend fun getAllSchools(): Response<SchoolResponse>

}