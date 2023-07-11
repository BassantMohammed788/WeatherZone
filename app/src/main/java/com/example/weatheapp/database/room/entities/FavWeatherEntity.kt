package com.example.weatheapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourite_table")
data class FavWeatherEntity(
    val latt: Double,
    val lng: Double,
    val city: String,
    @PrimaryKey
    var id : String = "$latt $lng $city"
)