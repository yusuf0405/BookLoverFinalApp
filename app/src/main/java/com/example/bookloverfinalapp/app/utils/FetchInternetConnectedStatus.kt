package com.example.bookloverfinalapp.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FetchInternetConnectedStatus {

    operator fun invoke(): Flow<Boolean>

}

class FetchInternetConnectedStatusImpl(
    private val context: Context
) : FetchInternetConnectedStatus {

    override fun invoke(): Flow<Boolean> = flow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    emit(true)
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    emit(true)
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    emit(true)
                }
            }
        }
        emit(false)
    }

}