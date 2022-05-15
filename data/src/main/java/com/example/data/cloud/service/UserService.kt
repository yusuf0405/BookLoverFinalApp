package com.example.data.cloud.service

import com.example.data.cloud.models.*
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

    @PUT("users/{id}")
    suspend fun updateStudentClass(
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("id") id: String,
        @Body updateClass: UpdateStudentClassCloud,
    ): Response<Unit>

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("id") id: String,
    ): Response<Unit>

    @GET("users/me")
    suspend fun getMe(
        @Header("X-Parse-Session-Token") sessionToken: String,
    ): Response<UserCloud>

    @PUT("users/{id}")
    suspend fun addSessionToken(
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("id") id: String,
        @Body userSessionToken: SessionTokenCloud,
    ): Response<Unit>
}