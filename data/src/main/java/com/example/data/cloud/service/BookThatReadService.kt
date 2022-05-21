package com.example.data.cloud.service

import com.example.data.cloud.models.*
import retrofit2.Response
import retrofit2.http.*

interface BookThatReadService {

    @GET("classes/BooksThatRead")
    suspend fun fetchMyBooks(
        @Query("where") id: String,
    ): Response<BooksThatReadResponse>


    @DELETE("classes/BooksThatRead/{id}")
    suspend fun deleteMyBook(
        @Path("id") id: String,
    ): Response<Unit>


    @GET("classes/BooksThatRead")
    suspend fun getMyBook(
        @Query("where") id: String,
    ): Response<BooksThatReadResponse>

    @GET("classes/Books")
    suspend fun getBook(
        @Query("where") id: String,
    ): Response<BookResponse>


    @POST("classes/BooksThatRead")
    suspend fun addNewBookStudent(
        @Body book: AddNewBookThatReadCloud,
    ): Response<PostRequestAnswerCloud>


    @PUT("classes/BooksThatRead/{id}")
    suspend fun bookThatReadUpdateProgress(
        @Path("id") id: String,
        @Body progress: UpdateProgressCloud,
    ): Response<Unit>


    @PUT("classes/BooksThatRead/{id}")
    suspend fun bookThatReadUpdatePages(
        @Path("id") id: String,
        @Body chapters: UpdateChaptersCloud,
    ): Response<Unit>
}