package com.example.data.base

import com.example.data.cloud.CloudDataRequestState
import com.example.domain.Mapper
import retrofit2.Response

interface ResponseHandler {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
    ): CloudDataRequestState<T>

    suspend fun <T, K> safeApiMapperCall(
        mapper: Mapper<T, K>,
        apiCall: suspend () -> Response<T>,
    ): CloudDataRequestState<K>
}