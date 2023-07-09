package com.example.weatheapp.network

import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.models.MyResponse

sealed class ApiState{
    class Success(val weather: MyResponse) : ApiState()
    class Failure(val message: Throwable) : ApiState()
    object Loading : ApiState()
}

