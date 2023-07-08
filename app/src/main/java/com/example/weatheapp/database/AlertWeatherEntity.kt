package com.example.weatheapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var startDate:Long,
    var endDate:Long,
    var startTime:Long,
    var endTime:Long
)
