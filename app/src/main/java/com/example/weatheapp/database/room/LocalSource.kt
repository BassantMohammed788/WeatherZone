package com.example.weatheapp.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface LocalSource {
    suspend fun  getFavWeather(): List<FavWeatherEntity>
    suspend fun insertFavWeather(favWeather:FavWeatherEntity)
    suspend fun deleteFavWeather(favWeather:FavWeatherEntity)
    suspend fun  getAlertWeather(): List<AlertWeatherEntity>
    suspend fun insertAlertWeather(alertWeather:AlertWeatherEntity)
    suspend fun deleteAlertWeather(alertWeather:AlertWeatherEntity)
    suspend fun insertHomeWeather(homeWeather:MyResponseEntity)
    suspend fun getSavedWeather(type: String, id: String):  MyResponseEntity

}