package com.example.weatheapp.home

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.utilities.Constants
import java.util.*

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val mySharedPreferences = MySharedPreferences.getInstance(this)
        changeAppLanguage()
        if (mySharedPreferences.getTempratureUnitPreference() == null)
        {
            mySharedPreferences.saveTempratureUnitPreference(Constants.standard.toString())
        }
        if (mySharedPreferences.getWindSpeedPreference() == null)
        {
            mySharedPreferences.saveTempratureUnitPreference(Constants.metr.toString())
        }

        Log.d("MyApp", "Locale set to ${mySharedPreferences.getTempratureUnitPreference()}")
    }

    private fun changeAppLanguage() {

        val mySharedPreferences = MySharedPreferences.getInstance(this)
        val language = mySharedPreferences.getLanguagePreference()
        if (language == null) {
            mySharedPreferences.saveLanguagePreference(Constants.en.toString())
        }
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
      Log.d("MyApp", "Locale set to $language")
    }
}