package com.example.weatheapp.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkCapabilities: NetworkCapabilities? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    }
    val isConnected =
        networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    return isConnected
}