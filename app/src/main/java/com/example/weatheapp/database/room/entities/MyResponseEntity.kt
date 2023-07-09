package com.example.weatheapp.database

import androidx.room.*
import com.example.weatheapp.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "home_table")
data class MyResponseEntity(
    @PrimaryKey val id: Int = 1,
    val countryName: String?,
    var lat: Double, var lon: Double,
    var current: Current?,
    var hourly: List<Hourly>?,
    var daily: List<Daily>?,
    var alert: List<Alert>?
) {

}


class ResponseEntityConverter {

    @TypeConverter
    fun fromCurrentToString(current: Current): String = Gson().toJson(current)

    @TypeConverter
    fun fromStringToCurrent(current: String): Current =
        Gson().fromJson(current, Current::class.java)

    @TypeConverter
    fun fromTempToString(temp: Temperature): String = Gson().toJson(temp)

    @TypeConverter
    fun fromStringToTemp(temp: String): Temperature = Gson().fromJson(temp, Temperature::class.java)

    @TypeConverter
    fun fromWeatherToString(weather: Weather): String = Gson().toJson(weather)

    @TypeConverter
    fun fromStringToWeather(weather: String): Weather =
        Gson().fromJson(weather, Weather::class.java)

    @TypeConverter
    fun fromWeatherListToString(weather: List<Weather>): String = Gson().toJson(weather)

    @TypeConverter
    fun fromStringToWeatherList(weather: String) =
        Gson().fromJson(weather, Array<Weather>::class.java).toList()

    @TypeConverter
    fun fromHourlyToString(hourly: List<Hourly>): String = Gson().toJson(hourly)

    @TypeConverter
    fun fromStringToHourly(hourly: String) =
        Gson().fromJson(hourly, Array<Hourly>::class.java).toList()

    @TypeConverter
    fun fromDailyToString(daily: List<Daily>): String = Gson().toJson(daily)

    @TypeConverter
    fun fromStringToDaily(daily: String) = Gson().fromJson(daily, Array<Daily>::class.java).toList()

    @TypeConverter
    fun fromAlertListToString(alertList: List<Alert>?): String? {
        return if (alertList != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Alert>>() {}.type
            gson.toJson(alertList, type)
        } else {
            null
        }
    }

    @TypeConverter
    fun fromStringToAlertList(alertListString: String?): List<Alert>? {
        return if (alertListString != null) {
            val gson = Gson()
            val type = object : TypeToken<List<Alert>>() {}.type
            gson.fromJson(alertListString, type)
        } else {
            null
        }
    }
}