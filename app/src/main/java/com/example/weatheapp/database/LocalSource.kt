package com.example.weatheapp.database

interface LocalSource {
    suspend fun  getFavWeather(): List<FavWeatherPojo>
    suspend fun insertFavWeather(favWeather:FavWeatherPojo)
    suspend fun deleteFavWeather(favWeather:FavWeatherPojo)
}