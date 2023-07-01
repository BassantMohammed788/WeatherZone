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

    fun saveNotificationPreference(status: String) {
        sharedPreferences.edit().putString(Constants.NOTIFICATION_STATUS.toString(), status).apply()
    }

    fun getNotificationPreference(): String? {
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
}