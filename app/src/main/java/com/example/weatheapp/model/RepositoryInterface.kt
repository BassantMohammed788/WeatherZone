package com.example.weatheapp.model

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    suspend fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String): Flow<MyResponse>
}