package com.example.data.data.cloud.service

import com.example.data.data.cloud.models.UserCloud
import com.example.data.data.cloud.models.SignUpAnswerCloud
import com.example.data.data.cloud.models.UserSignUpCloud
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {


    @POST("login")
    suspend fun signIn(
        @Header("X-Parse-Revocable-Session") session: Int,
        @Query("username") username: String,
        @Query("password") password: String,
    ): Response<UserCloud>

    @POST("users")
    suspend fun signUp(
        @Header("X-Parse-Revocable-Session") session: Int,
        @Body user: UserSignUpCloud,
    ): Response<SignUpAnswerCloud>

}