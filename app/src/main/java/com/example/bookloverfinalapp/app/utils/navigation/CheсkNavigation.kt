package com.example.bookloverfinalapp.app.utils.navigation

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.bookloverfinalapp.app.utils.APP_NAV_OBSERVER_KEY
import com.example.bookloverfinalapp.app.utils.APP_NAV_WRITE_KEY

class CheсkNavigation {

    fun observeLogin(status: Boolean, activity: Activity) =
        activity.getSharedPreferences(APP_NAV_OBSERVER_KEY, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(APP_NAV_WRITE_KEY, status)
            .apply()


    fun readLoginStatus(activity: Activity): Boolean =
        activity.getSharedPreferences(APP_NAV_OBSERVER_KEY, Context.MODE_PRIVATE)
            .getBoolean(APP_NAV_WRITE_KEY, false)


    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}