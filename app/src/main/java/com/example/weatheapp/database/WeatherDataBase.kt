package com.example.weatheapp.database

import android.content.Context
import androidx.room.*

@Database(entities = [FavWeatherEntity::class,AlertWeatherEntity::class,MyResponseEntity::class] , version = 1)
@TypeConverters(Converter::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getFavouriteWeatherDao(): FavoriteWeatherDAO
    abstract fun getAlertWeatherDao():ALertWeatherDAO
    abstract fun getHomeWeatherDao():HomeWeatherDAO
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