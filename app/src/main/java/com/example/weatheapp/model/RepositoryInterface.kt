package com.example.weatheapp.model

import com.example.weatheapp.database.FavWeatherPojo
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String): Flow<MyResponse>
    suspend fun  getFavWeather(): Flow<List<FavWeatherPojo>>
    suspend fun insertFavWeather(favWeather: FavWeatherPojo)
    suspend fun deleteFavWeather(favWeather: FavWeatherPojo)
}