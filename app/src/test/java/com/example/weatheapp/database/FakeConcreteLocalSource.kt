package com.example.weatheapp.database

class FakeConcreteLocalSource(
    var myResponseList: MutableList<MyResponseEntity> = mutableListOf<MyResponseEntity>(),
    var myAlertList:  MutableList<AlertWeatherEntity> = mutableListOf<AlertWeatherEntity>(),
    var myFavList: MutableList<FavWeatherEntity> = mutableListOf<FavWeatherEntity>()

):LocalSource {
    override suspend fun getFavWeather(): List<FavWeatherEntity> {
        return myFavList
    }

    override suspend fun insertFavWeather(favWeather: FavWeatherEntity) {
        myFavList.add(favWeather)
    }

    override suspend fun deleteFavWeather(favWeather: FavWeatherEntity) {
        myFavList.remove(favWeather)
    }

    override suspend fun getAlertWeather(): List<AlertWeatherEntity> {
        return myAlertList
    }

    override suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity) {
        myAlertList.add(alertWeather)
    }

    override suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity) {
        myAlertList.remove(alertWeather)
    }

    override suspend fun insertHomeWeather(homeWeather: MyResponseEntity) {
        myResponseList.add(homeWeather)
    }

    override suspend fun getSavedWeather(type: String, id: String): MyResponseEntity {
        return myResponseList.last()
    }
}