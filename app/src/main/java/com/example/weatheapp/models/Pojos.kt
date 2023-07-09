package com.example.weatheapp.models

data class MyResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Int,
    val current: Current,
    val minutely: List<MinutelyWeather>,
    val hourly: List<Hourly>,
    val daily: List<Daily>,
    val alerts: List<Alert>? )

data class Current(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Double,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<Weather>
)

data class MinutelyWeather(
    val dt: Long,
    val precipitation: Double
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    val feelsLike: Double,
    val weather: List<Weather>,
)

data class Daily(
    val dt: Long,
    val temp: Temperature,
    val feelsLike: Double,
    val weather: List<Weather>,
)

data class Alert(
    val senderName: String,
    val event: String,
    val start: Long,
    val end: Long,
    val description: String,
    var tags: List<String>
)

data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)