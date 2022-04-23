package com.example.data.data.cloud.service

import com.example.data.data.cloud.models.BookResponse
import com.example.data.data.cloud.models.BooksThatReadResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudentBookService {

    @GET("classes/BooksThatRead")
    suspend fun fetchMyBooks(
        @Query("where") id: String,
    ): Response<BooksThatReadResponse>


    @DELETE("classes/BooksThatRead/{id}")
    suspend fun deleteMyBook(
        @Path("id") id: String,
    ): Response<Unit>

    @GET("classes/Books")
    suspend fun getBook(
        @Query("where") id: String,
    ): Response<BookResponse>
}