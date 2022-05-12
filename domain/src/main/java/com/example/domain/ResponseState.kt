package com.example.domain

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EMPTY,
}

data class Resource<T>(
    val status: Status,
    var data: T?,
    val message: String?,
    val exception: Exception?,
) {
    companion object {

        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null, exception = null)

        fun <T> error(message: String?): Resource<T> =
            Resource(status = Status.ERROR, data = null, message = message, exception = null)

        fun <T> empty(): Resource<T> =
            Resource(status = Status.EMPTY, data = null, exception = null, message = null)

        fun <T> loading(): Resource<T> =
            Resource(status = Status.LOADING, data = null, message = null, exception = null)
    }
}

