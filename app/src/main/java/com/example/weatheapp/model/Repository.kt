package com.example.weatheapp.model

import android.util.Log
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.LocalSource
import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


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

    override suspend fun getFavWeather(): Flow<List<FavWeatherEntity>> {
        Log.i("TAG", "getFavWeather: ${localSource.getFavWeather()}")
        return flowOf(localSource.getFavWeather())
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherEntity) {
        localSource.insertFavWeather(favWeather)
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherEntity) {
        localSource.deleteFavWeather(favWeather)
    }

    override suspend fun getAlertWeather(): Flow<List<AlertWeatherEntity>> {
        return flowOf(localSource.getAlertWeather())
    }

    override suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity) {
        localSource.insertAlertWeather(alertWeather)
    }

    override suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity) {
       localSource.deleteAlertWeather(alertWeather)
    }

    override suspend fun getHomeWeather(): Flow<MyResponseEntity> {

        Log.i("TAG", "getWeatherFromRoom:${localSource.getHomeWeather()} ")
        return flowOf( localSource.getHomeWeather())
    }

    override suspend fun insertHomeWeather(homeWeather: MyResponseEntity) {
        localSource.insertHomeWeather(homeWeather)
    }
}


