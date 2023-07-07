package com.example.weatheapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val alertStartDate: String,
    var alertEndDate: String,
    var alertStartTime: String,
    var alertEndTime: String,
    var startUnix:Long,
    var endUnix:Long
)
