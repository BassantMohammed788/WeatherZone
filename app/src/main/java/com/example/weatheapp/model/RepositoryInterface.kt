package com.example.weatheapp.model

import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.MyResponseEntity
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String): Flow<MyResponse>
    suspend fun  getFavWeather(): Flow<List<FavWeatherEntity>>
    suspend fun insertFavWeather(favWeather: FavWeatherEntity)
    suspend fun deleteFavWeather(favWeather: FavWeatherEntity)

    suspend fun  getAlertWeather(): Flow<List<AlertWeatherEntity>>
    suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity)
    suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity)

    suspend fun  getHomeWeather(): Flow<MyResponseEntity>
    suspend fun insertHomeWeather(homeWeather: MyResponseEntity)
}