package com.example.weatheapp.model

import android.util.Log
import com.example.weatheapp.database.FavWeatherPojo
import com.example.weatheapp.database.LocalSource
import com.example.weatheapp.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.math.log


class Repository private constructor( var remoteSource: RemoteSource,var localSource: LocalSource) : RepositoryInterface{

    companion object{
        private var instance : Repository? = null
        fun getInstance(remoteSource: RemoteSource,localSource: LocalSource):Repository{
            return instance?: synchronized(this){
                val temp = Repository(remoteSource,localSource)
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

    override suspend fun getFavWeather(): Flow<List<FavWeatherPojo>> {
        Log.i("TAG", "getFavWeather: ${localSource.getFavWeather()}")
        return flowOf(localSource.getFavWeather())
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherPojo) {
        localSource.insertFavWeather(favWeather)
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherPojo) {
        localSource.deleteFavWeather(favWeather)
    }
}


