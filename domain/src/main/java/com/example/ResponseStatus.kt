package com.example

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

sealed class ResponseStatus<T> {

    class Success<T>(data: T) : ResponseStatus<T>()

    class Error<T>(message: String, error: Throwable) : ResponseStatus<T>()

    class Loading<T> : ResponseStatus<T>()

}

interface Request<T> {

    fun execute(callback: Callback<T>)

    fun cancel()

    interface Callback<T> {

        fun onSuccess(value: T)

        fun onError(e: Exception)
    }
}

fun <T> Request<T>.asFlow(): Flow<T> {
    return callbackFlow {
        execute(callback = object : Request.Callback<T> {

            override fun onSuccess(value: T) {
                trySend(value)
                close()
            }

            override fun onError(e: Exception) {
                close(e)
            }
        })
        awaitClose { this@asFlow.cancel() }
    }
}

class Test {

    fun dd(): Request<String> {

        val dsd = object : Request<String> {
            override fun execute(callback: Request.Callback<String>) {
                callback.onSuccess("dsd")
            }

            override fun cancel() {


            }
        }


        return dsd
    }

    fun dwddwdwd() {
        val dd = object : Request.Callback<String> {

            override fun onSuccess(value: String) {

            }

            override fun onError(e: Exception) {

            }

        }
        dd().execute(dd)

    }
}