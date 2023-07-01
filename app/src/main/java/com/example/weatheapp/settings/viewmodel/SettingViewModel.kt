package com.example.weatheapp.settings.viewmodel

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.example.weatheapp.MySharedPreferences
import java.util.*


class SettingViewModel(val mySharedPreferences: MySharedPreferences) : ViewModel() {

    fun setLanguage(language: String, languageCode: String) {
        changeAppLanguage(languageCode)
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
    private fun changeAppLanguage(lan: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lan)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}

