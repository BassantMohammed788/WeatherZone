package com.example.weatheapp.network

import com.example.weatheapp.model.MyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody


class ApiClient private constructor() : RemoteSource {
    val apiService: WeatherService by lazy {
        RetrofitHelper.retrofitInstance.create(WeatherService::class.java)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                val temp = ApiClient()
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