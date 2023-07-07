package com.example.weatheapp.network

import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.model.MyResponse

sealed class ApiState{
    class Success(val weather: MyResponse) : ApiState()
    class Failure(val message: Throwable) : ApiState()
    object Loading : ApiState()
}

sealed class RoomState{
    class Success(val weather: MyResponseEntity) : RoomState()
    class Failure(val message: Throwable) : RoomState()
    object Loading : RoomState()
}
