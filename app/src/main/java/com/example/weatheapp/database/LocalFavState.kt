package com.example.weatheapp.database


sealed class LocalFavState{
    class Success(val favWeather: List<FavWeatherPojo>) : LocalFavState()
    class Failure(val message: Throwable) : LocalFavState()
    object Loading : LocalFavState()
}

