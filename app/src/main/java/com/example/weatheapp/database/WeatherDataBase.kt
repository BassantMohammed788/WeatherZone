package com.example.weatheapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavWeatherPojo::class] , version = 1)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getFavouriteWeatherDao(): FavoriteWeatherDAO
    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getInstance(context: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java, "weather_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}