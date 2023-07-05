package com.example.weatheapp.home

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.utilities.Constants
import java.util.*

class MyApp : Application() {
    lateinit var  mySharedPreferences : MySharedPreferences
    override fun onCreate() {
        super.onCreate()
        mySharedPreferences = MySharedPreferences.getInstance(this)
        val language = mySharedPreferences.getLanguagePreference()
        if (language == null) {
            mySharedPreferences.saveLanguagePreference(Constants.en.toString())
            Log.d("MyApp", "First Locale set to $language")
        }else{
            Log.d("MyApp", "Locale set to $language")
        }

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

        if (mySharedPreferences.getTempratureUnitPreference() == null)
        {
            mySharedPreferences.saveTempratureUnitPreference(Constants.standard.toString())
        }
        if (mySharedPreferences.getWindSpeedPreference() == null)
        {
            mySharedPreferences.saveWindSpeedUnitPreference(Constants.metr.toString())
        }

        Log.d("MyApp", "Locale set to ${mySharedPreferences.getTempratureUnitPreference()}")
        Log.d("MyApp", "Locale set to ${mySharedPreferences.getWindSpeedPreference()}")
    }

}