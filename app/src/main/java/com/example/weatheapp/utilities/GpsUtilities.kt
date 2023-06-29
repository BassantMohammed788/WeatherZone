import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.location.*

const val PERMISSION_ID = 500
private val myLocation = MyLocation
private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

fun checkPermission(context: Context): Boolean {
    var result = ActivityCompat.checkSelfPermission(context,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
    return result
}

fun requestPermission(context: Context) {
    ActivityCompat.requestPermissions(context as Activity,
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        PERMISSION_ID
    )
}

private fun isLocationEnabled(context: Context): Boolean {
    val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

@SuppressLint("MissingPermission")
fun requestNewLocationData(context: Context, callback: (location: MyLocation?) -> Unit) {
    val locationRequest = LocationRequest()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 0

    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            // Location found
            myLocation.lat = location.latitude
            myLocation.lng = location.longitude
            callback(myLocation)
        } else {
            // Location not found, request new location data
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        val currentLocation = locationResult.lastLocation
                        // Convert Location object to MyLocation object
                        myLocation.lat =currentLocation.latitude
                        myLocation.lng =currentLocation.longitude
                        callback(myLocation)
                        // Stop location updates after the current location is obtained
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }, Looper.getMainLooper()
            )
        }
    }
}

@SuppressLint("MissingPermission")
fun getLastLocation(context: Context, callback: (location: MyLocation?) -> Unit) {
    if (checkPermission(context)) {
        if (isLocationEnabled(context)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Convert Location object to MyLocation object
                    val myLocation = MyLocation
                    myLocation.lng = location.longitude
                    myLocation.lat = location.latitude
                    callback(myLocation)
                } else {
                    // Location not found, request new location data
                    requestNewLocationData(context, callback)
                }
            }
        } else {
            // Location is not enabled
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(context, intent, null)
            callback(null)
        }
    } else {
        // Permission not granted
        requestPermission(context)
        callback(null)
    }
}