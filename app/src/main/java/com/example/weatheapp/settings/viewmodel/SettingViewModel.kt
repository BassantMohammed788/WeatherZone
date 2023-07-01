package com.example.weatheapp.settings.viewmodel

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
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

    private fun changeAppLanguage(lan: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lan)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}

