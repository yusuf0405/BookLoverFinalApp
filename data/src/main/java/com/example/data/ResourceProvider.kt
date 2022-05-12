package com.example.data

import android.content.Context
import androidx.annotation.StringRes
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ResourceProvider {

    fun getString(@StringRes id: Int): String

    fun errorType(exception: Exception): String

    fun errorType(exception: Throwable): String


    class Base(private val context: Context) : ResourceProvider {

        override fun getString(id: Int) = context.getString(id)

        override fun errorType(exception: Exception): String {
            return when (exception) {
                is UnknownHostException -> getString(R.string.network_error)
                is SocketTimeoutException -> getString(R.string.network_error)
                is ConnectException -> getString(R.string.network_error)
                is HttpException -> getString(R.string.service_unavailable)
                else -> getString(R.string.generic_error)
            }
        }

        override fun errorType(exception: Throwable): String {
            return when (exception) {
                is UnknownHostException -> getString(R.string.network_error)
                is SocketTimeoutException -> getString(R.string.network_error)
                is ConnectException -> getString(R.string.network_error)
                is HttpException -> getString(R.string.service_unavailable)
                else -> getString(R.string.generic_error)
            }
        }
    }
}