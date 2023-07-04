package com.example.weatheapp.database

import androidx.room.*


@Dao
interface FavoriteWeatherDAO {
    @Query("Select * from favourite_table")
    suspend fun  getFavWeather(): List<FavWeatherPojo>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavWeather(favWeather:FavWeatherPojo)
    @Delete
    suspend fun deleteFavWeather(favWeather:FavWeatherPojo)
}
