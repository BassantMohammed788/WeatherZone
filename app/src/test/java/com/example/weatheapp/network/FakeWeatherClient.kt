package com.example.weatheapp.network

import com.example.weatheapp.models.MyResponse

class FakeWeatherClient(val myResponse: MyResponse) :RemoteSource{
    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): MyResponse {
        return myResponse
    }
}