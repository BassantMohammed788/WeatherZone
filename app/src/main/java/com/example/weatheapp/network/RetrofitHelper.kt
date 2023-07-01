package com.example.weatheapp.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val gson = GsonBuilder().create()
    val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}