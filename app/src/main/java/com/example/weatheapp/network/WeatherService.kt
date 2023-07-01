package com.example.weatheapp.network

import com.example.weatheapp.model.MyResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {


    @GET("onecall?appid=fd166031d1e2e464cdec6f60cf9b2f3a&exclude=minutely")
    suspend fun getWeatherOverNetwork(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
    ): MyResponse
}
