package com.example.weatheapp.database


sealed class LocalFavState{
    class Success(val favWeather: List<FavWeatherEntity>) : LocalFavState()
    class Failure(val message: Throwable) : LocalFavState()
    object Loading : LocalFavState()
}
sealed class LocalAlertState{
    class Success(val alertWeather: List<AlertWeatherEntity>) : LocalAlertState()
    class Failure(val message: Throwable) : LocalAlertState()
    object Loading : LocalAlertState()
}

