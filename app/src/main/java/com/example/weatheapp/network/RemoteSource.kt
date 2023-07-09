package com.example.weatheapp.network

import com.example.weatheapp.models.MyResponse

interface RemoteSource {
    suspend fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String): MyResponse
}