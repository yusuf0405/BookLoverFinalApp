package com.example.data.cloud.service

import com.example.data.cloud.models.SchoolResponse
import retrofit2.Response
import retrofit2.http.GET


interface SchoolService {

    @GET("classes/School")
    suspend fun getAllSchools(): Response<SchoolResponse>

}