package com.example.weatheapp.settings.viewmodel

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.example.weatheapp.MySharedPreferences
import java.util.*


class SettingViewModel(val mySharedPreferences: MySharedPreferences) : ViewModel() {

    fun setLanguage(language: String, languageCode: String,context: Context) {
        changeAppLanguage(languageCode,context)
        mySharedPreferences.saveLanguagePreference(language)
    }

    fun getLanguage(): String? {
        return mySharedPreferences.getLanguagePreference()
    }

    fun setNotificationStatus(status: String) {
        mySharedPreferences.saveNotificationPreference(status)
    }

    fun getNotificationStatus(): String? {
        return mySharedPreferences.getNotificationPreference()
    }

    fun setLocationMethod(locationMethod: String) {
        mySharedPreferences.saveLocationMethodPreference(locationMethod)
    }

    fun getLocationMethod(): String? {
        return mySharedPreferences.getLocationMethodPreference()
    }

    fun setTempratureUnit(unit: String)
    {
        mySharedPreferences.saveTempratureUnitPreference(unit)
        Log.d("temp", "setTempratureUnit: hello from view model")
    }
    fun getTempratureUnit():String?
    {
        return mySharedPreferences.getTempratureUnitPreference()
    }
    fun setWindSpeedUnit(unit: String)
    {
        mySharedPreferences.saveWindSpeedUnitPreference(unit)
    }
    fun getWindSpeedUnit():String?
    {
        return mySharedPreferences.getWindSpeedPreference()
    }
    private fun changeAppLanguage(language: String,context:Context) {
       /* val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)*/
        val locale = Locale(language)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

    }

}

