package com.example.data.data.cloud.service

import com.example.data.data.cloud.models.BookQuestionResponse
import com.example.data.data.cloud.models.BookResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("classes/Books")
    suspend fun fetchAllBooks(): Response<BookResponse>

    @GET("classes/Questions")
    suspend fun getAllChapterQuestions(
        @Query("where") id: String,
    ): Response<BookQuestionResponse>
}