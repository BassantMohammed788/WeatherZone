package com.example.weatheapp.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.MySharedPreferences



class SettingViewModelFactory(private val mySharedPreferences: MySharedPreferences) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(mySharedPreferences) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}