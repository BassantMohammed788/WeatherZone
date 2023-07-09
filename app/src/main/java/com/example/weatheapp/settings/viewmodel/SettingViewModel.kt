package com.example.weatheapp.settings.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import com.example.weatheapp.MySharedPreferences



class SettingViewModel(val mySharedPreferences: MySharedPreferences) : ViewModel() {

    fun setLanguage(languageCode: String) {
       setAppLanguage(languageCode)
        mySharedPreferences.saveLanguagePreference(languageCode)
    }

    fun getLanguage(): String? {
        return mySharedPreferences.getLanguagePreference()
    }

    fun setNotificationStatus(status: String) {
        mySharedPreferences.saveNotificationStatusPreference(status)
    }

    fun getNotificationStatus(): String? {
        return mySharedPreferences.getNotificationStatusPreference()
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
    fun  setAppLanguage(language:String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

}