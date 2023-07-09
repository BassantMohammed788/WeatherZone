package com.example.weatheapp.utilities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.weatheapp.R
import java.text.SimpleDateFormat
import java.util.*


const val REQUEST_OVERLAY_PERMISSION = 1000

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

    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month - 1)
    cal.set(Calendar.DAY_OF_MONTH, day)

    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.timeInMillis
}
fun getTimeInMillis(hours: Int, minutes: Int): Long {
    val cal = Calendar.getInstance()

    cal.set(Calendar.HOUR_OF_DAY,hours)
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
@RequiresApi(Build.VERSION_CODES.M)
fun requestOverlayPermission(fragment: Fragment): Boolean {
    if (!Settings.canDrawOverlays(fragment.requireContext())) {
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setTitle(R.string.titleOverlaypermission)
        builder.setMessage(R.string.messageOverlaypermission)
        builder.setPositiveButton(R.string.grantPermissionBtn) { _, _ ->
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${fragment.requireContext().packageName}"))
            fragment.startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        }
        builder.setNegativeButton(R.string.CancelMapAlert) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
        return false
    } else {
        return true
    }
}