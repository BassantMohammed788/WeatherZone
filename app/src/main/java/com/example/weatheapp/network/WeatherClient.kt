package com.example.weatheapp.network

import com.example.weatheapp.models.MyResponse


class WeatherClient private constructor() : RemoteSource {
    val apiService: WeatherService by lazy {
        RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
    }

    companion object {
        private var instance: WeatherClient? = null
        fun getInstance(): WeatherClient {
            return instance ?: synchronized(this) {
                val temp = WeatherClient()
                instance = temp
                temp
            }
        }
    }

    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): MyResponse{
            val response = apiService.getWeatherOverNetwork(lat, lon, units, lang)
            return response
        }

}