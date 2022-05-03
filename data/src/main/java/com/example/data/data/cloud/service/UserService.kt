package com.example.data.data.cloud.service

import com.example.data.data.cloud.models.BooksThatReadResponse
import com.example.data.data.cloud.models.UserResponse
import com.example.domain.models.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("users")
    suspend fun fetchMyBook(@Query("where") id: String): Response<UserResponse>

    @GET("classes/BooksThatRead")
    suspend fun fetchStudentAttributes(@Query("where") id: String): Response<BooksThatReadResponse>
}