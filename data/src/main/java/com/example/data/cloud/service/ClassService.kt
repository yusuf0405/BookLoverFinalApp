package com.example.data.cloud.service

import com.example.data.cloud.models.AddClassCloud
import com.example.data.cloud.models.ClassResponse
import com.example.data.cloud.models.PostRequestAnswerCloud
import retrofit2.Response
import retrofit2.http.*

interface ClassService {

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassResponse>

    @DELETE("classes/Classes/{id}")
    suspend fun deleteClass(
        @Path("id") id: String,
    ): Response<Unit>


    @POST("classes/Classes")
    suspend fun addBookQuestion(
        @Body schoolClass: AddClassCloud,
    ): Response<PostRequestAnswerCloud>
}