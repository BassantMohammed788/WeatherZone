package com.example.weatheapp

import android.content.Context
import com.example.weatheapp.utilities.Constants

class MySharedPreferences private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: MySharedPreferences? = null

        fun getInstance(context: Context): MySharedPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = MySharedPreferences(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val sharedPreferences = context.getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE)

    fun saveLanguagePreference(language: String) {
        sharedPreferences.edit().putString(Constants.LANGUAGE.toString(), language).apply()
    }

    fun getLanguagePreference(): String? {
        return sharedPreferences.getString(Constants.LANGUAGE.toString(), Constants.en.toString())
    }

    fun saveLocationMethodPreference(locationMethod: String) {
        sharedPreferences.edit().putString(Constants.LOCATION_METHOD.toString(), locationMethod).apply()
    }

    fun getLocationMethodPreference(): String? {
        return sharedPreferences.getString(Constants.LOCATION_METHOD.toString(), null)
    }

    fun saveNotificationStatusPreference(status: String) {
        sharedPreferences.edit().putString(Constants.NOTIFICATION_STATUS.toString(), status).apply()
    }

    fun getNotificationStatusPreference(): String? {
        return sharedPreferences.getString(Constants.NOTIFICATION_STATUS.toString(), null)
    }
    fun saveTempratureUnitPreference(unit: String) {
        sharedPreferences.edit().putString(Constants.TempratureUnit.toString(), unit).apply()
    }

    fun getTempratureUnitPreference(): String? {
        return sharedPreferences.getString(Constants.TempratureUnit.toString(), null)
    }
    fun saveWindSpeedUnitPreference(unit: String) {
        sharedPreferences.edit().putString(Constants.WindUnit.toString(), unit).apply()
    }

    fun getWindSpeedPreference(): String? {
        return sharedPreferences.getString(Constants.WindUnit.toString(), null)
    }

    fun saveMapDestinationPrefrence(destination:String){
        sharedPreferences.edit().putString(Constants.MAP_DESTINATION.toString(),destination).apply()
    }
    fun getMapDestinationPrefrence():String?{
        return sharedPreferences.getString(Constants.MAP_DESTINATION.toString(),null)
    }

    fun saveFavLat(lat:String){
        sharedPreferences.edit().putString(Constants.FAVOURITE_LOCATION_LAT.toString(),lat).apply()
    }
    fun getFavLat():String?{
        return sharedPreferences.getString(Constants.FAVOURITE_LOCATION_LAT.toString(),null)
    }
    fun saveFavLng(lat:String){
        sharedPreferences.edit().putString(Constants.FAVOURITE_LOCATION_LNG.toString(),lat).apply()
    }
    fun getFavLng():String?{
        return sharedPreferences.getString(Constants.FAVOURITE_LOCATION_LNG.toString(),null)
    }
    fun saveHomeDestination(destination:String){
        sharedPreferences.edit().putString(Constants.HOME_DESTINATION.toString(),destination).apply()
    }
    fun getHomeDestination():String?{
        return sharedPreferences.getString(Constants.HOME_DESTINATION.toString(),null)
    }
}