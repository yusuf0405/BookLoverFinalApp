package com.example.bookloverfinalapp.app.utils

import com.example.bookloverfinalapp.R
import com.example.data.data.ResourceProvider
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorType @Inject constructor(private val resourceProvider: ResourceProvider) {

    fun errorMessage(exception: Exception): String =
        when (exception) {
            is UnknownHostException -> resourceProvider.getString(R.string.network_error)
            is HttpException -> resourceProvider.getString(R.string.service_unavailable)
            else -> resourceProvider.getString(R.string.generic_error)
        }

}