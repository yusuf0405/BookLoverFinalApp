package com.example.data.data.base

import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = withContext(Dispatchers.IO) { apiCall() }
            if (response.isSuccessful) {
                val body = withContext(Dispatchers.Default) { response.body() }
                body?.let { return Resource.success(data = body) }
            }
            return Resource.error(message = response.message())
        } catch (exception: Exception) {
            return when (exception) {
                is UnknownHostException -> Resource.error(message = "Нету подключение к интернету")
                is SocketTimeoutException -> Resource.error(message = "Нету подключение к интернету")
                is HttpException -> Resource.error(message = "Ошибка Сервера")
                else -> Resource.error(message = "Что то пошло не так")
            }

        }
    }
}