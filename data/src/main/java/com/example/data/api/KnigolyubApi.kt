package com.example.data.api

import com.example.data.data.cloud.models.BookQuestionResponse
import com.example.data.models.classes.ClassesResponse
import com.example.data.models.school.SchoolResponse
import com.example.data.models.student.*
import retrofit2.Response
import retrofit2.http.*


interface KnigolyubApi {

    @POST("login")
    suspend fun signIn(
        @Header("X-Parse-Revocable-Session") session: Int,
        @Query("username") username: String,
        @Query("password") password: String,
    ): Response<UserDto>

    @POST("users")
    suspend fun signUp(
        @Header("X-Parse-Revocable-Session") session: Int,
        @Body user: UserSignUpRequest,
    ): Response<PostRequestAnswerDto>


    @PUT("users/{id}")
    suspend fun updateUser(
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("id") id: String,
        @Body student: UserUpdateRequest,
    ): Response<UpdateDto>

    @GET("classes/Questions")
    suspend fun getAllChapterQuestions(
        @Query("where") id: String,
    ): Response<BookQuestionResponse>


    @GET("classes/Classes")
    suspend fun getAllClasses(): Response<ClassesResponse>

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassesResponse>

    @GET("classes/School")
    suspend fun getAllSchools(): Response<SchoolResponse>

}