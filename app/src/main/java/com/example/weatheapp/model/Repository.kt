package com.example.weatheapp.model

import android.util.Log
import com.example.weatheapp.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class Repository private constructor( var remoteSource: RemoteSource) : RepositoryInterface{

    companion object{
        private var instance : Repository? = null
        fun getInstance(remoteSource: RemoteSource):Repository{
            return instance?: synchronized(this){
                val temp = Repository(remoteSource)
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
    ): Flow<MyResponse> {
        Log.i("TAG", "getWeatherOverNetworkRepo: data${remoteSource.getWeatherOverNetwork(lat,lon,units,lang)}")
        return flowOf( remoteSource.getWeatherOverNetwork(lat,lon,units,lang))
    }
}


