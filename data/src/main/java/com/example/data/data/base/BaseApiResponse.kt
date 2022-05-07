package com.example.data.data.base

import android.util.Log
import com.example.data.R
import com.example.data.data.ResourceProvider
import com.example.domain.domain.Mapper
import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseApiResponse(private val resourceProvider: ResourceProvider) {

    suspend fun <T, K> safeApiMapperCall(
        mapper: Mapper<T, K>,
        apiCall: suspend () -> Response<T>,
    ): Resource<K> {
        try {
            val response = withContext(Dispatchers.IO) { apiCall() }
            if (response.isSuccessful) {
                val body = withContext(Dispatchers.Default) { response.body() }
                body?.let {
                    Log.i("ssss","sssssss")
                    return Resource.success(data = mapper.map(body)) }
            }
            return Resource.error(message = response.message())
        } catch (exception: Exception) {
            return when (exception) {
                is UnknownHostException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is SocketTimeoutException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is ConnectException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is HttpException -> Resource.error(resourceProvider.getString(R.string.service_unavailable))
                else -> Resource.error(resourceProvider.getString(R.string.generic_error))
            }

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
            return when (exception) {
                is UnknownHostException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is SocketTimeoutException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is ConnectException -> Resource.error(resourceProvider.getString(R.string.network_error))
                is HttpException -> Resource.error(resourceProvider.getString(R.string.service_unavailable))
                else -> Resource.error(resourceProvider.getString(R.string.generic_error))
            }

        }
    }

}