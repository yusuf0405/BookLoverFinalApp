package com.example.data.api

import com.example.data.models.book.*
import com.example.data.models.classes.ClassesResponse
import com.example.data.models.school.SchoolResponse
import com.example.data.models.student.*
import com.example.domain.models.book.AddNewBookRequest
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

    @GET("classes/Books")
    suspend fun getBook(
        @Query("where") id: String,
    ): Response<BookResponse>

    @GET("classes/BooksThatRead")
    suspend fun getAllStudentBooks(
        @Query("where") id: String,
    ): Response<BookStudentRequests>

    @GET("classes/BooksThatRead")
    suspend fun studentMyProgress(
        @Query("where") id: String,
    ): Response<BooksThatReadResponse>

    @GET("classes/Questions")
    suspend fun getAllChapterQuestions(
        @Query("where") id: String,
    ): Response<BookQuestionResponse>

    @PUT("classes/BooksThatRead/{id}")
    suspend fun updateProgressStudentBook(
        @Path("id") id: String,
        @Body progress: BookUpdateProgressRequest,
    ): Response<UpdateDto>


    @POST("classes/BooksThatRead")
    suspend fun addNewBookStudent(
        @Body book: AddNewBookRequest,
    ): Response<PostRequestAnswerDto>


    @GET("classes/Classes")
    suspend fun getAllClasses(): Response<ClassesResponse>

    @GET("classes/Classes")
    suspend fun getClass(
        @Query("where") id: String,
    ): Response<ClassesResponse>

    @GET("classes/School")
    suspend fun getAllSchools(): Response<SchoolResponse>

    @GET("classes/Books")
    suspend fun getAllBooks(): Response<BookResponse>
}