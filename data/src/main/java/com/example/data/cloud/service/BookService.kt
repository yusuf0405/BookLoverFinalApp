package com.example.data.cloud.service

import com.example.data.cloud.models.*
import retrofit2.Response
import retrofit2.http.*

interface BookService {

    @GET("classes/Books")
    suspend fun fetchAllBooks(): Response<BookResponse>

    @GET("classes/Questions")
    suspend fun getAllChapterQuestions(
        @Query("where") id: String,
    ): Response<BookQuestionResponse>

    @POST("classes/Questions")
    suspend fun addBookQuestion(
        @Body question: AddBookQuestionCloud,
    ): Response<Unit>

    @DELETE("classes/Questions/{id}")
    suspend fun deleteBookQuestion(
        @Path("id") id: String,
    ): Response<Unit>

    @PUT("classes/Questions/{id}")
    suspend fun updateQuestion(
        @Path("id") id: String,
        @Body question: AddBookQuestionCloud,
    ): Response<Unit>

    @POST("classes/Books")
    suspend fun addNewBook(
        @Body book: AddNewBookCloud,
    ): Response<PostRequestAnswerCloud>
}