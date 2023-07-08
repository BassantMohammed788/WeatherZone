package com.example.weatheapp.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

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
fun getDateInMillis(day: Int, month: Int, year: Int): Long {
    val cal = Calendar.getInstance()

    // Set the year, month, and day values
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, day)

    // Set the time to midnight
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    // Return the date in milliseconds
    return cal.timeInMillis
}
fun getTimeInMillis(hours: Int, minutes: Int, amPm: String): Long {
    val cal = Calendar.getInstance()

    cal.set(Calendar.HOUR_OF_DAY, when (amPm) {
        "AM","am","ص" -> hours % 12
        "PM","pm","م" -> (hours % 12) + 12
        else -> throw IllegalArgumentException("Invalid AM/PM format: $amPm")
    })
    cal.set(Calendar.MINUTE, minutes)

    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.timeInMillis
}
fun getTimeStringFromMillis(millis: Long, language: String): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = millis
    val dateFormat = SimpleDateFormat("h:mm a", Locale(language))
    return dateFormat.format(cal.time)
}
fun getDateStringFromMillis(millis: Long, language: String): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = millis
    val dateFormat = SimpleDateFormat("EEE d MMM", Locale(language))
    return dateFormat.format(cal.time)
}
fun getUnixTimestamp(hour: Int, amPm: String, minute: Int, day: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance()

    // Set the year, month, and day
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Note: month is 0-based (0 = January)
    calendar.set(Calendar.DAY_OF_MONTH, day)

    // Set the hour and minute
    val hourOfDay = if (amPm.equals("PM", ignoreCase = true)) {
        if (hour != 12) hour + 12 else 12
    } else {
        if (hour == 12) 0 else hour
    }
    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
    calendar.set(Calendar.MINUTE, minute)

    // Set the seconds and milliseconds to 0
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Convert the Calendar instance to a Unix timestamp
    return calendar.timeInMillis / 1000L
}