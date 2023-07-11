package com.example.weatheapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alert_table")
data class AlertWeatherEntity(
    @PrimaryKey()
    var id: String = UUID.randomUUID().toString(),
    var startDate:Long,
    var endDate:Long,
    var startTime:Long,
    var endTime:Long,
    var alertType:String
)
