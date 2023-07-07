package com.example.weatheapp.database

import android.content.Context
import android.util.Log


class ConcreteLocalSource (context: Context) : LocalSource {
    private val favoriteWeatherDAO: FavoriteWeatherDAO by lazy {
        val dataBase: WeatherDataBase = WeatherDataBase.getInstance(context)
        dataBase.getFavouriteWeatherDao()
    }
    private val alertWeatherDAO: ALertWeatherDAO by lazy {
        val dataBase: WeatherDataBase = WeatherDataBase.getInstance(context)
        dataBase.getAlertWeatherDao()
    }
    private val homeWeatherDAO: HomeWeatherDAO by lazy {
        val dataBase: WeatherDataBase = WeatherDataBase.getInstance(context)
        dataBase.getHomeWeatherDao()
    }

    override suspend fun getFavWeather(): List<FavWeatherEntity> {
        return favoriteWeatherDAO.getFavWeather()
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherEntity) {
        favoriteWeatherDAO.insertFavWeather(favWeather)
        Log.i("TAG", "insertFavWeather: fav saved Successfully")
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherEntity) {
        favoriteWeatherDAO.deleteFavWeather(favWeather)
    }

    override suspend fun getAlertWeather(): List<AlertWeatherEntity> {
        return alertWeatherDAO.getAlertWeather()
    }

    override suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity) {
       alertWeatherDAO.insertAlertWeather(alertWeather)
    }

    override suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity) {
        alertWeatherDAO.deleteAlertWeather(alertWeather)
    }

    override suspend fun getHomeWeather(): MyResponseEntity {
        return homeWeatherDAO.getHomeWeather()
    }

    override suspend fun insertHomeWeather(homeWeather: MyResponseEntity) {
        homeWeatherDAO.insertHomeWeather(homeWeather)
    }


}