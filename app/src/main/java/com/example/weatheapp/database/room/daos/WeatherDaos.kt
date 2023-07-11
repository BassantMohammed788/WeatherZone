package com.example.weatheapp.database

import androidx.room.*


@Dao
interface HomeWeatherDAO {

    @Query("SELECT * FROM home_table WHERE type = :type AND id = :id")
    suspend fun getSavedWeather(type: String, id: String):   MyResponseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomeWeather(homeWeather: MyResponseEntity)
}

@Dao
interface FavoriteWeatherDAO {
    @Query("Select * from favourite_table")
    suspend fun getFavWeather(): List<FavWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeather(favWeather: FavWeatherEntity)

    @Delete
    suspend fun deleteFavWeather(favWeather: FavWeatherEntity)
}

@Dao
interface ALertWeatherDAO {
    @Query("Select * from alert_table")
    suspend fun getAlertWeather(): List<AlertWeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertWeather(alertWeather: AlertWeatherEntity)

    @Delete
    suspend fun deleteAlertWeather(alertWeather: AlertWeatherEntity)
}