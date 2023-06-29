import android.content.Context
import android.content.SharedPreferences

object MyLocation {
    private const val PREFERENCES_FILE_NAME = "MyLocationPreferences"
    private const val KEY_LATITUDE = "latitude"
    private const val KEY_LONGITUDE = "longitude"

    private var sharedPreferences: SharedPreferences? = null

    var lat: Double? = null
        set(value) {
            field = value
            saveToSharedPreferences()
        }

    var lng: Double? = null
        set(value) {
            field = value
            saveToSharedPreferences()
        }

    fun loadFromSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        lat = sharedPreferences?.getFloat(KEY_LATITUDE, 0f)?.toDouble()
        lng = sharedPreferences?.getFloat(KEY_LONGITUDE, 0f)?.toDouble()
    }

    private fun saveToSharedPreferences() {
        sharedPreferences?.edit()?.apply {
            lat?.let { putFloat(KEY_LATITUDE, it.toFloat()) }
            lng?.let { putFloat(KEY_LONGITUDE, it.toFloat()) }
            apply()
        }
    }
}