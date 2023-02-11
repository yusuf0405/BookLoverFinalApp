package com.example.data.repository

import com.example.data.cloud.CloudDataRequestState
import com.example.domain.RequestState

interface BaseRepository {

    suspend fun <T> renderResultToUnit(
        result: CloudDataRequestState<T>,
        onSuccess: (suspend (T) -> Unit?)? = null,
        onError: ((Throwable) -> Unit?)? = null,
    ) = when (result) {
        is CloudDataRequestState.Success -> {
            onSuccess?.invoke(result.data)
            RequestState.Success(Unit)
        }
        is CloudDataRequestState.Error -> {
            onError?.invoke(result.error)
            RequestState.Error(result.error)
        }
    }

    suspend fun <T> renderResult(
        result: CloudDataRequestState<T>,
        onSuccess: (suspend (T) -> Unit?)? = null,
        onError: ((Throwable) -> Unit?)? = null,
    ) = when (result) {
        is CloudDataRequestState.Success -> {
            onSuccess?.invoke(result.data)
            RequestState.Success(result.data)
        }
        is CloudDataRequestState.Error -> {
            onError?.invoke(result.error)
            RequestState.Error(result.error)
        }
    }
}