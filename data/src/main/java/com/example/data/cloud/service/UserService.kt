package com.example.data.cloud.service

import com.example.data.cloud.models.BooksThatReadResponse
import com.example.data.cloud.models.UserResponse
import com.example.data.cloud.models.UpdateCloud
import com.example.data.cloud.models.UserUpdateCloud
import retrofit2.Response
import retrofit2.http.*

interface UserService {

    @GET("users")
    suspend fun fetchMyBook(@Query("where") id: String): Response<UserResponse>

    @GET("classes/BooksThatRead")
    suspend fun fetchStudentAttributes(@Query("where") id: String): Response<BooksThatReadResponse>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("id") id: String,
        @Body student: UserUpdateCloud,
    ): Response<UpdateCloud>
}