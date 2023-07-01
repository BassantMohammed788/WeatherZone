package com.example.weatheapp.network

import com.example.weatheapp.model.MyResponse

sealed class ApiState{
    class Success(val weather: MyResponse) : ApiState()
    class Failure(val message: Throwable) : ApiState()
    object Loading : ApiState()
}
