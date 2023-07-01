package com.example.weatheapp.network

import com.example.weatheapp.model.MyResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {


    @GET("onecall?appid=87efa574d0e1e382f33b0f42eaba9da1&exclude=minutely")
    suspend fun getWeatherOverNetwork(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
    ): MyResponse
}
