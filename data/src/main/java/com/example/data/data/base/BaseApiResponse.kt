package com.example.data.data.base

import android.util.Log
import com.example.data.R
import com.example.data.data.ResourceProvider
import com.example.domain.domain.Mapper
import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

abstract class BaseApiResponse(private val resourceProvider: ResourceProvider) {

    suspend fun <T, K> safeApiMapperCall(
        mapper: Mapper<T, K>,
        apiCall: suspend () -> Response<T>,
    ): Resource<K> {
        try {
            val response = withContext(Dispatchers.IO) { apiCall() }
            if (response.isSuccessful) {
                val body = withContext(Dispatchers.Default) { response.body() }
                body?.let { return Resource.success(data = mapper.map(body)) }
            }
            return when {
                response.code() == 400 -> Resource.error(message = resourceProvider.getString(R.string.account_already))
                response.code() == 404 -> Resource.error(message = resourceProvider.getString(R.string.invalid_email_password))
                else -> Resource.error(message = response.message())
            }
        } catch (exception: Exception) {
            return Resource.error(message = resourceProvider.errorType(exception = exception))
        }
    }

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>,
    ): Resource<T> {
        try {
            val response = withContext(Dispatchers.IO) { apiCall() }
            if (response.isSuccessful) {
                val body = withContext(Dispatchers.Default) { response.body() }
                body?.let { return Resource.success(data = body) }
            }
            return Resource.error(message = response.message())
        } catch (exception: Exception) {
            return Resource.error(message = resourceProvider.errorType(exception = exception))
        }
    }

}