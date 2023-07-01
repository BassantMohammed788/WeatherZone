package com.example.weatheapp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
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
        return sharedPreferences.getString(Constants.LANGUAGE.toString(), Constants.ENGLISH.toString())
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
}