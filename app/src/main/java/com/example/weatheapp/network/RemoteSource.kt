package com.example.weatheapp.network

import com.example.weatheapp.model.MyResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.Query

interface RemoteSource {
    suspend fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String): MyResponse
}