package com.example.data.data.cloud.service

import com.example.data.data.cloud.models.BookResponse
import retrofit2.Response
import retrofit2.http.GET

interface BookService {

    @GET("classes/Books")
    suspend fun fetchAllBooks(): Response<BookResponse>
}