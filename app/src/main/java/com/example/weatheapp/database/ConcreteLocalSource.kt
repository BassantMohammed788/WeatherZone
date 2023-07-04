package com.example.weatheapp.database

import android.content.Context
import android.util.Log


class ConcreteLocalSource (context: Context) : LocalSource {
    private val favoriteWeatherDAO: FavoriteWeatherDAO by lazy {
        val dataBase: WeatherDataBase = WeatherDataBase.getInstance(context)
        dataBase.getFavouriteWeatherDao()
    }

    override suspend fun getFavWeather(): List<FavWeatherPojo> {
        return favoriteWeatherDAO.getFavWeather()
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherPojo) {
        favoriteWeatherDAO.insertFavWeather(favWeather)
        Log.i("TAG", "insertFavWeather: fav saved Successfully")
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherPojo) {
        favoriteWeatherDAO.deleteFavWeather(favWeather)
    }


}