package com.example.weatheapp.models

import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.MyResponseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepository(var myRemoteResponse:MyResponse,
                     var myResponseLocalList: MutableList<MyResponseEntity> = mutableListOf<MyResponseEntity>(),
                     var myAlertList:  MutableList<AlertWeatherEntity> = mutableListOf<AlertWeatherEntity>(),
                     var myFavList: MutableList<FavWeatherEntity> = mutableListOf<FavWeatherEntity>()
) :RepositoryInterface
{
    override suspend fun getWeatherOverNetwork(
        lat: Double,
        lon: Double,
        units: String,
        lang: String
    ): Flow<MyResponse> {
        return flowOf(myRemoteResponse)
    }

    override suspend fun getFavWeather(): Flow<List<FavWeatherEntity>> {
        return flowOf(myFavList)
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherEntity) {
       myFavList.add(favWeather)
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherEntity) {
       myFavList.remove(favWeather)
    }

    override suspend fun getAlertWeather(): Flow<List<AlertWeatherEntity>> {
        return flowOf(myAlertList)
    }

    override suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity) {
        myAlertList.add(alertWeather)
    }

    override suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity) {
        myAlertList.remove(alertWeather)
    }

    override suspend fun insertHomeWeather(homeWeather: MyResponseEntity) {
       myResponseLocalList.add(homeWeather)
    }

    override suspend fun getSavedWeather(type: String, id: String): Flow<MyResponseEntity> {
       return flowOf(myResponseLocalList.last())
    }
}