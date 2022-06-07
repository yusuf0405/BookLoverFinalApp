package com.example.data.cloud.service

import com.example.data.cloud.models.*
import retrofit2.Response
import retrofit2.http.*

interface BookService {

    @GET("classes/Books")
    suspend fun fetchAllBooks(
        @Query("where") id: String,
    ): Response<BookResponse>

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

    @PUT("classes/Books/{id}")
    suspend fun updateBook(
        @Path("id") id: String,
        @Body book: UpdateBookCloud,
    ): Response<Unit>

    @DELETE("classes/Books/{id}")
    suspend fun deleteBook(
        @Path("id") id: String,
    ): Response<Unit>


    @GET("classes/BooksThatRead")
    suspend fun fetchMyBooks(
        @Query("where") id: String,
    ): Response<BooksThatReadResponse>

    @DELETE("classes/BooksThatRead/{id}")
    suspend fun deleteMyBook(
        @Path("id") id: String,
    ): Response<Unit>


    @POST("classes/Books")
    suspend fun addNewBook(
        @Body book: AddNewBookCloud,
    ): Response<PostRequestAnswerCloud>
}